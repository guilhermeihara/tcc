package hyperledger.cefetmg.tcc.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationService authenticationService;

	// Authentication Configuration
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
	}

	// Authorization Configuration
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub

//		http.authorizeRequests().anyRequest().authenticated().and().formLogin();
		http.formLogin();
		// .antMatchers("/permited").permitAll() Permit all request on /permited
		// .antMatchers(HttpMethod.GET,"/permited/*").permitAll() Permit get request on
		// /permited

		super.configure(http);
	}

	// Static Resources Configurations js, css, images etc
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		super.configure(web);
	}
}
