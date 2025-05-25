package info.saladlam.example.spring.noticeboard.config;

import info.saladlam.example.spring.noticeboard.framework.CspHeaderWriter;
import info.saladlam.example.spring.noticeboard.framework.CspNonceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity(debug = false)
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
				// must put more restricted rule at first
				.requestMatchers("/manage/*/approve").hasAuthority("ADMIN")
				.requestMatchers("/manage/**").hasAuthority("USER")
				.requestMatchers("/**").permitAll()
			)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(customizer -> customizer
				.loginPage("/login")
				.loginProcessingUrl("/loginHandler")
				.failureUrl("/login?error=true")
				.permitAll()
			)
			.logout(customizer -> customizer
				.logoutSuccessUrl("/")
			)
			.headers(customizer -> customizer
				.addHeaderWriter(new CspHeaderWriter())
			)
			.addFilterAfter(new CspNonceFilter(), AuthorizationFilter.class);

		return http.build();
	}

}
