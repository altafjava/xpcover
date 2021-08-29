package com.gmc.main.jwt;

import java.util.Date;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	@Value("${jwt.expirationInMS}")
	private int jwtExpirationInMs;
	@Autowired
	private JwtGenerator jwtGenerator;

	public String createJwtUser(String id, Set<String> roles) {
		JwtUser jwtUser = new JwtUser();
		jwtUser.setId(id);
		jwtUser.setRoles(roles);
		Date date = new Date();
		jwtUser.setIssuedAt(date);
		jwtUser.setExpiration(new Date(date.getTime() + jwtExpirationInMs));
		return jwtGenerator.generate(jwtUser);
	}
}
