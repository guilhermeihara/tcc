package hyperledger.cefetmg.tcc.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hyperledger.cefetmg.tcc.services.HyperledgerService;

@RestController
@RequestMapping("/")
public class TransationController {
	
	HyperledgerService hyperledgerService = new HyperledgerService();
	
	@GetMapping
	public String lista() {
		try {
			hyperledgerService.createTransaction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Hello World";	
	}
	
}
