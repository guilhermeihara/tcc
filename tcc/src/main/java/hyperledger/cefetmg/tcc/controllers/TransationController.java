package hyperledger.cefetmg.tcc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hyperledger.cefetmg.tcc.dto.DtoAsset;
import hyperledger.cefetmg.tcc.services.HyperledgerService;

@RestController
@RequestMapping("/")
public class TransationController {
	
	@Autowired
	HyperledgerService _hyperledgerService;
	
	
	@GetMapping("/teste")
	public String getAss() {
		return "Hello World";
	}
	
	@GetMapping("/assets")
	public String getAssets() {
		return _hyperledgerService.getAssets();
	}
	
	@PostMapping("/assets")
	public String updateAsset(@RequestBody DtoAsset asset) {
		
		System.out.println("Teste POST: "+asset.toString());
		_hyperledgerService.createAsset(asset);
		return "";
	}
	
}
