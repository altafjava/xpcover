package com.gmc.employer.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gmc.employer.dto.CreateEmployerDTO;
import com.gmc.employer.dto.UpdateEmployerDTO;
import com.gmc.employer.model.Employer;
import com.gmc.employer.service.EmployerService;

@RestController
@RequestMapping("/api/v1/employers")
public class EmployerController {

	@Value("${jwt.prefixLength}")
	private int jwtPrefixLength;
	@Autowired
	private EmployerService employerService;

	@GetMapping("/{employerId}")
	public ResponseEntity<Object> getEmployer(@RequestHeader("Authorization") String token, @PathVariable String employerId) {
		Employer employer = employerService.getEmployer(token.substring(jwtPrefixLength), employerId);
		if (employer == null) {
			return new ResponseEntity<>("No Employer Found with id: " + employerId, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(employer, HttpStatus.OK);
		}

	}

	@GetMapping
	public ResponseEntity<List<Employer>> getEmployers(@RequestHeader("Authorization") String token) {
		List<Employer> employers = employerService.getEmployers(token.substring(jwtPrefixLength));
		return new ResponseEntity<>(employers, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Employer> createEmployer(@RequestBody CreateEmployerDTO createEmployerDTO) {
		Employer createdEmployer = employerService.createEmployer(createEmployerDTO);
		if (createdEmployer == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(createdEmployer, HttpStatus.CREATED);
		}
	}

	@PutMapping
	public ResponseEntity<Employer> updateEmployer(@RequestHeader("Authorization") String token, @RequestBody UpdateEmployerDTO updateEmployerDTO) {
		Employer updatedEmployer = employerService.updateEmployer(token.substring(jwtPrefixLength), updateEmployerDTO);
		return new ResponseEntity<>(updatedEmployer, HttpStatus.OK);
	}

	@DeleteMapping("/{employerId}")
	public ResponseEntity<Void> removeEmployer(@RequestHeader("Authorization") String token, @PathVariable String employerId) {
		employerService.removeEmployer(token.substring(jwtPrefixLength), employerId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
