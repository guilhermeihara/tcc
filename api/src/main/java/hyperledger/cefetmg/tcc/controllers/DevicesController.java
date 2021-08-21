package hyperledger.cefetmg.tcc.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import hyperledger.cefetmg.tcc.config.security.TokenService;
import hyperledger.cefetmg.tcc.dto.DtoAsset;
import hyperledger.cefetmg.tcc.dto.DtoDevice;
import hyperledger.cefetmg.tcc.dto.DtoTransaction;
import hyperledger.cefetmg.tcc.form.DeviceForm;
import hyperledger.cefetmg.tcc.form.TransactionForm;
import hyperledger.cefetmg.tcc.form.UpdateDeviceForm;
import hyperledger.cefetmg.tcc.models.Device;
import hyperledger.cefetmg.tcc.models.Token;
import hyperledger.cefetmg.tcc.models.User;
import hyperledger.cefetmg.tcc.repository.DeviceRepository;
import hyperledger.cefetmg.tcc.repository.TokenRepository;
import hyperledger.cefetmg.tcc.repository.UserRepository;
import hyperledger.cefetmg.tcc.services.HyperledgerDeviceService;

@RestController
@RequestMapping("/devices")
public class DevicesController {

	@Autowired
	private TokenService _tokenService;
	@Autowired
	private DeviceRepository _deviceRepository;
	@Autowired
	private UserRepository _userRepository;
	@Autowired
	private TokenRepository _tokenRepository;
	@Autowired
	HyperledgerDeviceService _hyperledgerService;

	@PostMapping
	@Transactional
	public ResponseEntity<DtoDevice> addDevice(HttpServletRequest request, @RequestBody @Valid DeviceForm deviceForm,
			UriComponentsBuilder uriBuilder) {
		Long userId = _tokenService.getUserId(request);
		Optional<User> user = _userRepository.findById(userId);

		Optional<Device> hasDevice = _deviceRepository.findByAddress(deviceForm.getAddress());

		if (hasDevice.isEmpty()) {
			Device device = new Device(user.get(), deviceForm.getAddress(), deviceForm.getName());
			_deviceRepository.save(device);

			String token = _tokenService.generateToken(device.getAddress(), deviceForm.getTokenDuration());
			Token deviceToken = new Token(token, deviceForm.getTokenDuration());
			_tokenRepository.save(deviceToken);
			device.setToken(deviceToken);

			if (deviceForm.getValue() != null && !deviceForm.getValue().isEmpty()) {
				device.setValue(deviceForm.getValue());
			} else {
				device.setValue("empty");
			}

			DtoAsset dtoAsset = new DtoAsset(device.getName(), device.getValue(), device.getAddress(),
					user.get().getEmail());

			try {
				boolean created = _hyperledgerService.createAsset(dtoAsset);
				if (!created) {
					_deviceRepository.delete(device);
				}
			} catch (Exception e) {
				_deviceRepository.delete(device);
			}
			_deviceRepository.save(device);
			return ResponseEntity.ok(new DtoDevice(device));
		}
		return ResponseEntity.badRequest().build();
//		return new ResponseEntity<DtoDevice>(null, null, HttpStatus.SC_BAD_REQUEST);
	}

	@GetMapping("/{address}")
	public ResponseEntity<DtoDevice> getDevice(HttpServletRequest request, @PathVariable String address) {
		Long userId = _tokenService.getUserId(request);
		System.out.println(userId);

		Optional<Device> device = _deviceRepository.findByAddress(address);

		if (device.isPresent()) {
			Long deviceUserId = device.get().getUser().getId();
			if (userId.equals(deviceUserId)) {
				return ResponseEntity.ok(new DtoDevice(device.get()));
			}
		}
		return ResponseEntity.notFound().build();
	}

	@PatchMapping
	@Transactional
	public ResponseEntity<DtoTransaction> updateDevice(HttpServletRequest request,
			@RequestBody @Valid UpdateDeviceForm updateForm) {
		String deviceId = updateForm.getAddress();
		Optional<Device> device = _deviceRepository.findByAddress(deviceId);

		if (device.isPresent()) {
			Device assetDevice = device.get();
			String deviceName = updateForm.getName() == null ? assetDevice.getName() : updateForm.getName();
			String deviceValue = updateForm.getValue() == null ? assetDevice.getValue() : updateForm.getValue();

			DtoAsset dtoAsset = new DtoAsset(deviceName, deviceValue, assetDevice.getAddress());
			boolean validTransaction = _hyperledgerService.updateAsset(dtoAsset);
			if (validTransaction) {
				assetDevice.setName(deviceName);
				assetDevice.setValue(deviceValue);
				_deviceRepository.save(assetDevice);
			}

			return ResponseEntity.ok(new DtoTransaction());
		}
		return ResponseEntity.badRequest().build();
	}

	@DeleteMapping
	@Transactional
	public ResponseEntity<DtoTransaction> deleteDevice(HttpServletRequest request,
			@RequestBody @Valid UpdateDeviceForm updateForm) {
		String deviceId = updateForm.getAddress();
		Optional<Device> device = _deviceRepository.findByAddress(deviceId);

		if (device.isPresent()) {
			Device assetDevice = device.get();
			_deviceRepository.delete(assetDevice);
			_hyperledgerService.deleteAsset(deviceId);
		}

		return ResponseEntity.ok(new DtoTransaction());
	}

	@PostMapping("/value")
	@Transactional
	public ResponseEntity<DtoTransaction> updateDeviceValue(HttpServletRequest request,
			@RequestBody @Valid TransactionForm transactionForm) {

		String deviceId = _tokenService.getDeviceId(request);

		Optional<Device> device = _deviceRepository.findByAddress(deviceId);
		if (device.isPresent()) {
			Device assetDevice = device.get();
			DtoAsset dtoAsset = new DtoAsset(assetDevice.getName(), transactionForm.getValue(),
					assetDevice.getAddress());
			boolean validTransaction = _hyperledgerService.updateAsset(dtoAsset);
			if (validTransaction) {
				assetDevice.setValue(transactionForm.getValue());
			}
			return ResponseEntity.ok(new DtoTransaction());
		}
		return ResponseEntity.badRequest().build();
	}

}
