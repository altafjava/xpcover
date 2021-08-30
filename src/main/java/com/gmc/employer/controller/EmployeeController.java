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
import com.gmc.employer.dto.EmployeeDto;
import com.gmc.employer.model.Employee;
import com.gmc.employer.service.EmployeeService;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

	@Value("${jwt.prefixLength}")
	private int jwtPrefixLength;
	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/{employeeId}")
	public ResponseEntity<Object> getEmployer(@RequestHeader("Authorization") String token, @PathVariable String employeeId) {
		Employee employee = employeeService.getEmployee(token.substring(jwtPrefixLength), employeeId);
		if (employee == null) {
			return new ResponseEntity<>("No Employee Found with id: " + employeeId, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(employee, HttpStatus.OK);
		}

	}

	@GetMapping
	public ResponseEntity<List<Employee>> getEmployees(@RequestHeader("Authorization") String token) {
		List<Employee> employees = employeeService.getEmployees(token.substring(jwtPrefixLength));
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Employee> addEmployee(@RequestHeader("Authorization") String token, @RequestBody EmployeeDto employeeDto) {
		Employee employee = employeeService.addEmployee(token.substring(jwtPrefixLength), employeeDto);
		if (employee == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(employee, HttpStatus.CREATED);
		}
	}

	@PutMapping("/{employeeId}")
	public ResponseEntity<Employee> updateEmployee(@RequestHeader("Authorization") String token, @RequestBody EmployeeDto employeeDto, @PathVariable String employeeId) {
		Employee updatedEmployee = employeeService.updateEmployee(token.substring(jwtPrefixLength), employeeDto, employeeId);
		return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
	}

	@DeleteMapping("/{employeeId}")
	public ResponseEntity<Void> removeEmployer(@RequestHeader("Authorization") String token, @PathVariable String employeeId) {
		employeeService.removeEmployee(token.substring(jwtPrefixLength), employeeId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
