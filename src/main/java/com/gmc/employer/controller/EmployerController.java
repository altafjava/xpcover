package com.gmc.employer.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gmc.employer.dto.CreateEmployerDTO;
import com.gmc.employer.dto.UpdateEmployerDTO;
import com.gmc.employer.model.Employer;
import com.gmc.employer.service.EmployerService;

@RestController
@RequestMapping("/api/v1/employers")
public class EmployerController {

	@Autowired
	private EmployerService employerService;

	@GetMapping("/{employerId}")
	public ResponseEntity<Object> getEmployer(@PathVariable String employerId) {
		Employer employer = employerService.getEmployer(employerId);
		if (employer == null) {
			return new ResponseEntity<>("No Employer Found with id: " + employerId, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(employer, HttpStatus.OK);
		}

	}

	@GetMapping
	public ResponseEntity<List<Employer>> getEmployers() {
		List<Employer> employers = employerService.getEmployers();
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
	public ResponseEntity<Employer> updateEmployer(@RequestBody UpdateEmployerDTO updateEmployerDTO) {
		Employer updatedEmployer = employerService.updateEmployer(updateEmployerDTO);
		return new ResponseEntity<>(updatedEmployer, HttpStatus.OK);
	}

	@DeleteMapping("/{employerId}")
	public ResponseEntity<Void> removeEmployer(@PathVariable String employerId) {
		employerService.removeEmployer(employerId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
