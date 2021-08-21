package hyperledger.cefetmg.tcc.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import hyperledger.cefetmg.tcc.repository.UserRepository;

//@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private TokenService _tokenService;

	@Autowired
	private AuthenticationService _authenticationService;

	@Autowired
	private UserRepository _userRepository;

	// Authentication Configuration
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(_authenticationService).passwordEncoder(new BCryptPasswordEncoder());
	}

	// Authorization Configuration
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub

		http.authorizeRequests().antMatchers("/devices/value*").permitAll().antMatchers(HttpMethod.POST, "/auth").permitAll().anyRequest().authenticated().and()
				.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterBefore(new TokenAuthenticationFilter(_tokenService, _userRepository),
						UsernamePasswordAuthenticationFilter.class);
		// http.authorizeRequests().anyRequest().authenticated().and().formLogin();
		// .antMatchers("/permited").permitAll() Permit all request on /permited
		// .antMatchers(HttpMethod.GET,"/permited/*").permitAll() Permit get request on
		// /permited

//		super.configure(http);
	}

	// Static Resources Configurations js, css, images etc
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**",
				"/swagger-resources/**");
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}
}
