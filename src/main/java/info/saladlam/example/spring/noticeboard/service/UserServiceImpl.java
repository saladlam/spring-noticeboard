package info.saladlam.example.spring.noticeboard.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.saladlam.example.spring.noticeboard.dto.UserDto;
import info.saladlam.example.spring.noticeboard.entity.Authority;
import info.saladlam.example.spring.noticeboard.entity.User;
import info.saladlam.example.spring.noticeboard.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<UserDto> loadUserByUsername(String username) {
		Optional<User> optionalUser = this.userRepository.findById(username);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			Set<String> grantedAuthority = new HashSet<>();
			for (Authority authority : user.getAuthorities()) {
				grantedAuthority.add(authority.getName());
			}

			UserDto ret = new UserDto();
			ret.setUsername(user.getUsername());
			ret.setPassword(user.getPassword());
			ret.setDisabled(Integer.valueOf(1).equals(user.getEnabled())? Boolean.FALSE: Boolean.TRUE);
			ret.setAuthorities(grantedAuthority);
			return Optional.of(ret);
		} else {
			return Optional.empty();
		}
	}

}
