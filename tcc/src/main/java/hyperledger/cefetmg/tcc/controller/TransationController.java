package hyperledger.cefetmg.tcc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hyperledger.cefetmg.tcc.dto.DtoAsset;
import hyperledger.cefetmg.tcc.service.HyperledgerService;

@RestController
@RequestMapping("/")
public class TransationController {
	
	HyperledgerService hyperledgerService = new HyperledgerService();
	
	@GetMapping("/assets")
	public String getAssets() {
		return hyperledgerService.getAssets();
	}
	
	@PostMapping("/assets")
	public String updateAsset(@RequestBody DtoAsset asset) {
		
		System.out.println("Teste POST: "+asset.toString());
		hyperledgerService.createAsset(asset);
		return "";
	}
	
}
