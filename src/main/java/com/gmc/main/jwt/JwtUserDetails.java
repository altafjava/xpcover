package com.gmc.main.jwt;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class JwtUserDetails implements UserDetails {

	private static final long serialVersionUID = 2483170093697337488L;
	private String id;
	private Date issuedAt;
	private Date expiration;
	private String token;
	private Set<String> roles;
//	private Collection<? extends GrantedAuthority> authorities;

	public JwtUserDetails() {
		super();
	}

	public JwtUserDetails(String id, Date issuedAt, Date expiration, String token, Set<String> roles) {
		super();
		this.id = id;
		this.issuedAt = issuedAt;
		this.expiration = expiration;
		this.token = token;
		this.roles = roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return null;
	}

}
