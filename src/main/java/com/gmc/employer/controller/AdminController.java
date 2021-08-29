package com.gmc.employer.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gmc.employer.dto.PolicyDto;
import com.gmc.employer.dto.Premium;
import com.gmc.employer.dto.Relationship;
import com.gmc.employer.dto.RelationshipOption;
import com.gmc.employer.model.Employer;
import com.gmc.employer.service.AdminService;

@RestController
@RequestMapping("/api/v1/employers")
public class AdminController {

	@Value("${jwt.prefixLength}")
	private int jwtPrefixLength;
	@Autowired
	private AdminService adminService;

	@PostMapping("/total-premium-pool")
	public ResponseEntity<Employer> addTotalPremiumPool(@RequestHeader("Authorization") String token, @RequestBody Premium premiumPool) {
		Employer employer = adminService.addTotalPremiumPool(token.substring(jwtPrefixLength), premiumPool);
		if (employer == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(employer, HttpStatus.OK);
		}
	}

	@GetMapping("/allowed-relationships")
	public ResponseEntity<List<Relationship>> getAllowedRelationships() {
		List<Relationship> allowedRelationships = adminService.getAllowedRelationships();
		return new ResponseEntity<>(allowedRelationships, HttpStatus.OK);
	}

	@GetMapping("/relationship-options")
	public ResponseEntity<RelationshipOption> getRelationshipOptions() {
		RelationshipOption relationshipOption = adminService.getRelationshipOptions();
		return new ResponseEntity<>(relationshipOption, HttpStatus.OK);
	}

	@PostMapping("/add-policy")
	public ResponseEntity<Employer> addPolicy(@RequestHeader("Authorization") String token, @RequestBody PolicyDto policyDto) {
		Employer employer = adminService.addPolicy(token.substring(jwtPrefixLength), policyDto);
		return new ResponseEntity<>(employer, HttpStatus.OK);
	}
}
