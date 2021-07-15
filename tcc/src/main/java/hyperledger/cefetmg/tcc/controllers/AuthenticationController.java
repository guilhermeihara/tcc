package hyperledger.cefetmg.tcc.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hyperledger.cefetmg.tcc.config.security.TokenService;
import hyperledger.cefetmg.tcc.dto.DtoToken;
import hyperledger.cefetmg.tcc.form.LoginForm;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager _authManager;

	@Autowired
	private TokenService _tokenService;

	@PostMapping
	public ResponseEntity<DtoToken> authenticate(@RequestBody @Valid LoginForm form) {
		UsernamePasswordAuthenticationToken loginData = form.converter();

		try {
			Authentication authentication = _authManager.authenticate(loginData);
			String token = _tokenService.generateToken(authentication);
			return ResponseEntity.ok(new DtoToken(token, "Bearer"));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}

	}
}
