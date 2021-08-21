//package hyperledger.cefetmg.tcc.controllers;
//
//import java.util.Optional;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.transaction.Transactional;
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import hyperledger.cefetmg.tcc.config.security.TokenService;
//import hyperledger.cefetmg.tcc.dto.DtoAsset;
//import hyperledger.cefetmg.tcc.dto.DtoTransaction;
//import hyperledger.cefetmg.tcc.form.TransactionForm;
//import hyperledger.cefetmg.tcc.models.Device;
//import hyperledger.cefetmg.tcc.repository.DeviceRepository;
//import hyperledger.cefetmg.tcc.services.HyperledgerService;
//
//@RestController
//@RequestMapping("/transaction")
//public class TransactionController {
//
//	@Autowired
//	HyperledgerService _hyperledgerService;
//	@Autowired
//	private DeviceRepository _deviceRepository;
////	@Autowired
////	private TransactionRepository _transactionRepository;
//
//	@Autowired
//	private TokenService _tokenService;
//
//	@GetMapping("/teste")
//	public String getAss(HttpServletRequest request) {
////		String ip = HttpUtils.getRequestIP(request);
//		return _hyperledgerService.getAssets();
////		return ip;
////		return "Hello World";
//	}
//
//	@GetMapping("/assets")
//	public ResponseEntity<String> getAssetValue(HttpServletRequest request) {
//		Long deviceId = _tokenService.getDeviceId(request);
//		String asset = _hyperledgerService.getAssetValue(deviceId);
//		return ResponseEntity.ok(asset);
//	}
//
//	@GetMapping("/assets/all")
//	public ResponseEntity<String> getAllAsset(HttpServletRequest request) {
////		Long deviceId = _tokenService.getDeviceId(request);
//		String asset = _hyperledgerService.getAssets();
//		return ResponseEntity.ok(asset);
//	}
//
//	@PostMapping("/assets")
//	@Transactional
//	public ResponseEntity<DtoTransaction> createTransaction(HttpServletRequest request,
//			@RequestBody @Valid TransactionForm transactionForm) {
////		System.out.println("OI 0");
//		Long deviceId = _tokenService.getDeviceId(request);
////		System.out.println("OI 1");
//		Optional<Device> device = _deviceRepository.findById(deviceId);
////		System.out.println("OI 2");
//		if (device.isPresent()) {
//			Device assetDevice = device.get();
////			String originIp = HttpUtils.getRequestIP(request);
////			System.out.println("OI 3");
//			DtoAsset dtoAsset = new DtoAsset(String.valueOf(assetDevice.getId()) + assetDevice.getAddress(),
//					assetDevice.getName(), transactionForm.getValue(), assetDevice.getAddress());
////			System.out.println("OI 4");
//			boolean validTransaction = _hyperledgerService.updateAsset(dtoAsset);
////			System.out.println("OI 5");
//			// Mysql
////			Transaction transaction = new Transaction(assetDevice.getName(), assetDevice.getValue(),
////					transactionForm.getValue(), transactionForm.getValue(), originIp, "generateId",
////					validTransaction ? "Valid" : "Invalid");
//			// _transactionRepository.save(transaction);
////			System.out.println("OI 6");
//			if (validTransaction) {
//				assetDevice.setValue(transactionForm.getValue());
//			}
////			System.out.println("OI 7");
//			return ResponseEntity.ok(new DtoTransaction());
//		}
//		return ResponseEntity.badRequest().build();
//	}
//
//}
