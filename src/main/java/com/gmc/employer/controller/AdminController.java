package com.gmc.employer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gmc.employer.dto.PremiumPoolDTO;
import com.gmc.employer.model.Employer;
import com.gmc.employer.service.AdminService;

@RestController
@RequestMapping("/api/v1/employers/admin")
public class AdminController {

	@Value("${jwt.prefixLength}")
	private int jwtPrefixLength;
	@Autowired
	private AdminService adminService;

	@PostMapping("/total-premium-pool")
	public ResponseEntity<Employer> addTotalPremiumPool(@RequestHeader("Authorization") String token, @RequestBody PremiumPoolDTO premiumPool) {
		Employer employer = adminService.addTotalPremiumPool(token.substring(jwtPrefixLength), premiumPool);
		if (employer == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(employer, HttpStatus.OK);
		}
	}
}
