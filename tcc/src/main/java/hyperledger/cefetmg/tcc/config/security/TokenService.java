package hyperledger.cefetmg.tcc.config.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import hyperledger.cefetmg.tcc.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${tcc.jwt.expiration}")
	private String expiration;

	@Value("${tcc.jwt.secret}")
	private String secret;

	@Value("${tcc.jwt.secretDevices}")
	private String secretDevices;

	public String generateToken(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return generateToken(user.getId().toString(), Long.parseLong(expiration), secret);
	}

	public String generateToken(String subject, Long tokenExpiration) {
		return generateToken(subject, tokenExpiration, secretDevices);
	}

	private String generateToken(String subject, Long tokenExpiration, String secret) {
		Date today = new Date();
		Date expirationDate = new Date(today.getTime() + tokenExpiration);

		return Jwts.builder().setIssuer("TCC Taka").setSubject(subject).setIssuedAt(today).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public boolean isValidToken(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getUserId(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

	public Long getUserId(HttpServletRequest request) {
		String token = retriveToken(request);

		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

	public String retriveToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}

		return token.substring(7, token.length());
	}
}
