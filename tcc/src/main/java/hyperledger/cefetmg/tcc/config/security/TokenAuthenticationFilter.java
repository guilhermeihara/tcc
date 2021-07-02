package hyperledger.cefetmg.tcc.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import hyperledger.cefetmg.tcc.models.User;
import hyperledger.cefetmg.tcc.repository.UserRepository;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private TokenService _tokenService;
	private UserRepository _userRepository;

	public TokenAuthenticationFilter(TokenService tokenService, UserRepository userRepository) {
		this._tokenService = tokenService;
		this._userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = retriveToken(request);

		boolean valid = _tokenService.isValidToken(token);

		if (valid) {
			authenticateToken(token);
		}

		filterChain.doFilter(request, response);
	}

	private void authenticateToken(String token) {
		Long userId = _tokenService.getUserId(token);
		User user = _userRepository.findById(userId).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String retriveToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}

		return token.substring(7, token.length());
	}

}
