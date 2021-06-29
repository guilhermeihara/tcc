package hyperledger.cefetmg.tcc.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import hyperledger.cefetmg.tcc.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${tcc.jwt.expiration}")
	private String expiration;
	
	@Value("${tcc.jwt.secret}")
	private String secret;
	
	public String generateToken(Authentication authentication) {

		User user = (User) authentication.getPrincipal();
		Date today = new Date();
		Date expirationDate = new Date(today.getTime()+Long.parseLong(expiration));

		return Jwts.builder()
				.setIssuer("TCC Taka")
				.setSubject(user.getId().toString())
				.setIssuedAt(today)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
}
