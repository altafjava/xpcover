package com.gmc.employer.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.gmc.employer.model.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

	public List<Employee> findByEmployerId(String employerId);
	
}
