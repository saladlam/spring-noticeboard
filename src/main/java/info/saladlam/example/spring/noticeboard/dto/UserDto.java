package info.saladlam.example.spring.noticeboard.dto;

import java.util.Set;

public class UserDto {

	private String username;
	private String password;
	private Boolean disabled;
	private Set<String> authorities;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}

}
