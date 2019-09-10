package info.saladlam.example.spring.noticeboard.service;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import info.saladlam.example.spring.noticeboard.dto.UserDto;

@Service
public class SpringSecurityUserService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserDto> optionalUser = this.userService.loadUserByUsername(username);
		if (optionalUser.isPresent()) {
			UserDto user = optionalUser.get();
			return User
						.withUsername(user.getUsername())
						.password(user.getPassword())
						.disabled(user.getDisabled())
						.authorities(StringUtils.toStringArray(user.getAuthorities()))
						.build();
		} else {
			throw new UsernameNotFoundException(MessageFormat.format("Username {0} not found", username));
		}
	}

}
