package com.gmc.main.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${jwt.expirationInMS}")
	private int jwtExpirationInMs;

	public String generate(JwtUser jwtUser) {
		Claims claims = Jwts.claims().setSubject(jwtUser.getId());
		claims.setIssuedAt(new Date());
		claims.setExpiration(new Date(new Date().getTime() + jwtExpirationInMs));
		claims.put("role", jwtUser.getRoles());
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
		// returning the JWT Token
	}
}
