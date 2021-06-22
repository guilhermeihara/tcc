package hyperledger.cefetmg.tcc.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hyperledger.cefetmg.tcc.models.User;
import hyperledger.cefetmg.tcc.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = repository.findByEmail(username);

		if (user.isPresent()) {
			System.out.println(user.get().getPassword());
			return user.get();
		}

		throw new UsernameNotFoundException("Dados Inválidos!");
	}

}
