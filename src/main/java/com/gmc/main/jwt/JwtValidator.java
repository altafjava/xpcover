package com.gmc.main.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.gmc.main.exception.JWTMissingException;
import com.gmc.main.util.TokenBlackListCache;

@Component
public class JwtValidator {
	private static final Logger LOGGER = LogManager.getLogger(JwtValidator.class);

	@Value("${jwt.secret}")
	private String jwtSecret;
	@Autowired
	private TokenBlackListCache tokenBlackListCache;

	public JwtUser validate(String token) {
		LOGGER.info("Started validating token");
		JwtUser jwtUser = null;
		try {
			if (tokenBlackListCache.isTokenBlackListed(token)) {
				LOGGER.error("BlackList JWT token");
				throw new MalformedJwtException("BlackList JWT token");
			}
			Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
			jwtUser = new JwtUser();
			jwtUser.setId(claims.getSubject());
			jwtUser.setIssuedAt(claims.getIssuedAt());
			jwtUser.setExpiration(claims.getExpiration());
			jwtUser.setRoles((Set<String>) claims.get("roles"));

		} catch (SignatureException ex) {
			LOGGER.error("Invalid JWT signature = " + ex);
			throw new SignatureException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			LOGGER.error("Invalid JWT token = " + ex);
			throw new MalformedJwtException("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			LOGGER.error("Expired JWT token = " + ex);
			throw new JWTMissingException("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			LOGGER.error("Unsupported JWT token = " + ex);
			throw new UnsupportedJwtException("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			LOGGER.error("JWT claims string is empty = " + ex);
			throw new IllegalArgumentException("JWT claims string is empty");
		}
		return jwtUser;
	}
}
