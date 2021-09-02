package com.gmc.employer.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gmc.employer.dto.CreateEmployer;
import com.gmc.employer.dto.UpdateEmployer;
import com.gmc.employer.model.Employer;
import com.gmc.employer.service.EmployerService;

@RestController
@RequestMapping("/api/v1/employers")
public class EmployerController {

	@Value("${jwt.prefixLength}")
	private int jwtPrefixLength;
	@Autowired
	private EmployerService employerService;

	@PreAuthorize("hasAuthority('OWNER')")
	@GetMapping("/{employerId}")
	public ResponseEntity<Object> getEmployer(@RequestHeader("Authorization") String token, @PathVariable String employerId) {
		Employer employer = employerService.getEmployer(employerId);
		if (employer == null) {
			return new ResponseEntity<>("No Employer Found with id: " + employerId, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(employer, HttpStatus.OK);
		}
	}

	@PreAuthorize("hasAuthority('OWNER')")
	@GetMapping
	public ResponseEntity<List<Employer>> getEmployers(@RequestHeader("Authorization") String token) {
		List<Employer> employers = employerService.getEmployers();
		return new ResponseEntity<>(employers, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('OWNER')")
	@PostMapping
	public ResponseEntity<Employer> createEmployer(@RequestHeader("Authorization") String token, @RequestBody CreateEmployer createEmployerDTO) {
		Employer createdEmployer = employerService.createEmployer(createEmployerDTO);
		if (createdEmployer == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(createdEmployer, HttpStatus.CREATED);
		}
	}

	@PreAuthorize("hasAuthority('OWNER')")
	@PutMapping("/{employerId}")
	public ResponseEntity<Employer> updateEmployer(@RequestHeader("Authorization") String token, @RequestBody UpdateEmployer updateEmployerDTO, @PathVariable String employerId) {
		Employer updatedEmployer = employerService.updateEmployer(employerId, updateEmployerDTO);
		return new ResponseEntity<>(updatedEmployer, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('OWNER')")
	@DeleteMapping("/{employerId}")
	public ResponseEntity<Void> removeEmployer(@RequestHeader("Authorization") String token, @PathVariable String employerId) {
		employerService.removeEmployer(employerId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
