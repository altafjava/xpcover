package com.gmc.employer.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gmc.employer.model.Employee;
import com.gmc.employer.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EmployeeDAO {

	@Autowired
	private EmployeeRepository employeeRepository;

	public Employee findEmployee(String employeeId) {
		log.info("Started fetching employee with id: {}", employeeId);
		Employee employee = null;
		Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
		if (employeeOptional.isEmpty()) {
			log.info("No employee found with this id: {}", employeeId);
		} else {
			employee = employeeOptional.get();
		}
		return employee;
	}

	public List<Employee> findAllEmployees(String employerId) {
		log.info("Started fetching list of employees");
		List<Employee> employees = employeeRepository.findByEmployerId(employerId);
		return employees;
	}

	public Employee saveEmployee(Employee employee) {
		Employee employee2 = employeeRepository.save(employee);
		log.info("Employee \'{}\' saved successfully with id: {}", employee2.getName(), employee2.getId());
		return employee2;
	}

	public Employee updateEmployee(Employee employee) {
		Employee employee2 = employeeRepository.save(employee);
		log.info("Employee \'{}\' updated successfully", employee2.getName());
		return employee2;
	}

	public void deleteEmployee(String employeeId) {
		employeeRepository.deleteById(employeeId);
		log.info("Employee with id: \'{}\' deleted successfully", employeeId);
	}
}
