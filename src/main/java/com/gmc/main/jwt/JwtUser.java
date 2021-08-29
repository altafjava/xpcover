package com.gmc.main.jwt;

import java.util.Date;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtUser {
	private String id;
	private Date issuedAt;
	private Date expiration;
	private Set<String> roles;
}
