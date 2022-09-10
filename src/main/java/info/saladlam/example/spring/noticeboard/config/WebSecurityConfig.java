package info.saladlam.example.spring.noticeboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// must put more restricted rule at first
				.antMatchers("/manage/*/approve").hasAuthority("ADMIN")
				.antMatchers("/manage/**").hasAuthority("USER")
			.and()
				.httpBasic().disable()
				.formLogin()
					.loginPage("/login")
					.loginProcessingUrl("/loginHandler")
					.failureUrl("/login?error=true")
					.permitAll()
			.and()
				.logout().logoutSuccessUrl("/");
	}

}
