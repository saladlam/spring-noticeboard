package info.saladlam.example.spring.noticeboard.service;

import java.util.Optional;

import info.saladlam.example.spring.noticeboard.dto.UserDto;

public interface UserService {

	Optional<UserDto> loadUserByUsername(String username);

}
