package com.gmc.main.jwt;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.gmc.main.constant.AppConstant;
import com.gmc.main.exception.JWTMissingException;
import com.gmc.main.util.EncryptorDecryptor;
import com.gmc.main.util.TokenBlackListCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtService {

	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${jwt.prefixLength}")
	private int jwtPrefixLength;
	@Value("${jwt.expirationInMS}")
	private int jwtExpirationInMs;
	@Autowired
	private TokenBlackListCache tokenBlackListCache;

	public String generateToken(Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Set<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
		String jwtToken = createJwtUser(userDetails.getId(), userDetails.getEmail(), roles);
		return jwtToken;
	}

	public String createJwtUser(String id, String email, Set<String> roles) {
		JwtUser jwtUser = new JwtUser();
		jwtUser.setId(id);
		jwtUser.setEmail(email);
		jwtUser.setRoles(roles);
		Date date = new Date();
		jwtUser.setIssuedAt(date);
		jwtUser.setExpiration(new Date(date.getTime() + jwtExpirationInMs));
		return generate(jwtUser);
	}

	private String generate(JwtUser jwtUser) {
		Claims claims = Jwts.claims().setSubject(jwtUser.getId());
		claims.setIssuedAt(new Date());
		claims.setExpiration(new Date(new Date().getTime() + jwtExpirationInMs));
		claims.put("email", EncryptorDecryptor.encrypt(jwtUser.getEmail()));
		claims.put("roles", jwtUser.getRoles());
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String parseJwtToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (StringUtils.hasText(header) && header.startsWith(AppConstant.BEARER)) {
			return header.substring(jwtPrefixLength, header.length());
		}
		return null;
	}

	public JwtUser validateJwtToken(String token) {
		log.info("Started validating token");
		JwtUser jwtUser = null;
		try {
			if (tokenBlackListCache.isTokenBlackListed(token)) {
				log.error("BlackList JWT token");
				throw new MalformedJwtException("BlackList JWT token");
			}
			Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
			jwtUser = new JwtUser();
			jwtUser.setId(claims.getSubject());
			jwtUser.setIssuedAt(claims.getIssuedAt());
			jwtUser.setExpiration(claims.getExpiration());
			jwtUser.setEmail(EncryptorDecryptor.decrypt(claims.get("email").toString()));
			Set<String> roles = new LinkedHashSet<>((List<String>) claims.get("roles"));
			jwtUser.setRoles(roles);
		} catch (SignatureException ex) {
			log.error("Invalid JWT signature = " + ex);
			throw new SignatureException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token = " + ex);
			throw new MalformedJwtException("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token = " + ex);
			throw new JWTMissingException("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token = " + ex);
			throw new UnsupportedJwtException("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty = " + ex);
			throw new IllegalArgumentException("JWT claims string is empty");
		}
		return jwtUser;
	}

	public String getIdFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
}
