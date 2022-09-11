package info.saladlam.example.spring.noticeboard.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {

	private String name;
	private String email;

	public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
					  Collection<? extends GrantedAuthority> authorities, String name, String email) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.name = name;
		this.email = email;
	}

	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String name, String email) {
		super(username, password, authorities);
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		String parent = super.toString();
		StringBuilder sb = new StringBuilder();
		sb.append(parent.substring(0, parent.length() - 1)).append(", ");
		sb.append("Name=").append(this.name).append(", ");
		sb.append("E-mail=").append(this.email).append("]");
		return sb.toString();
	}

}
