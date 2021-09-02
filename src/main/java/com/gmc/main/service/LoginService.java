package com.gmc.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.gmc.main.dto.LoginDTO;
import com.gmc.main.jwt.JwtService;

@Service
public class LoginService {

	@Autowired
	private JwtService jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;

	public ResponseEntity<String> login(LoginDTO loginDTO) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtUtil.generateToken(authentication);
		return ResponseEntity.ok(token);
	}
}
