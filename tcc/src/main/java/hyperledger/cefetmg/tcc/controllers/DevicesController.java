package hyperledger.cefetmg.tcc.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import hyperledger.cefetmg.tcc.config.security.TokenService;
import hyperledger.cefetmg.tcc.dto.DtoAsset;
import hyperledger.cefetmg.tcc.dto.DtoDevice;
import hyperledger.cefetmg.tcc.form.DeviceForm;
import hyperledger.cefetmg.tcc.models.Device;
import hyperledger.cefetmg.tcc.models.Token;
import hyperledger.cefetmg.tcc.models.User;
import hyperledger.cefetmg.tcc.repository.DeviceRepository;
import hyperledger.cefetmg.tcc.repository.TokenRepository;
import hyperledger.cefetmg.tcc.repository.UserRepository;
import hyperledger.cefetmg.tcc.services.HyperledgerService;

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
	HyperledgerService _hyperledgerService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<DtoDevice> addDevice(HttpServletRequest request, @RequestBody @Valid DeviceForm deviceForm, UriComponentsBuilder uriBuilder) {
		Long userId = _tokenService.getUserId(request);	
		Optional<User> user = _userRepository.findById(userId);
		Device device = new Device(user.get(),deviceForm.getType(),deviceForm.getName());			
		_deviceRepository.save(device);

		String token = _tokenService.generateToken(device.getId().toString(), deviceForm.getTokenDuration());
		Token deviceToken = new Token(token, deviceForm.getTokenDuration());
		_tokenRepository.save(deviceToken);
		device.setToken(deviceToken);
		
		if(deviceForm.getValue() != null && !deviceForm.getValue().isEmpty()) {
			device.setValue(deviceForm.getValue());
		}else {
			device.setValue("empty");
		}
		DtoAsset dtoAsset = new DtoAsset(device.getId().toString(), device.getName(), device.getValue());
		_hyperledgerService.createAsset(dtoAsset);

		return ResponseEntity.ok(new DtoDevice(device));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DtoDevice>  getDevice(HttpServletRequest request, @PathVariable Long id) {
		Long userId = _tokenService.getUserId(request);
		System.out.println(userId);
		
		Optional<Device> device = _deviceRepository.findById(id);
				
		if(device.isPresent()) {
			System.out.println(device.get().getUser().getId()); 
			Long deviceUserId = device.get().getUser().getId();
			if(userId.equals(deviceUserId)) {				
				return ResponseEntity.ok(new DtoDevice(device.get()));
			}
		}
		return ResponseEntity.notFound().build();
	}
}
