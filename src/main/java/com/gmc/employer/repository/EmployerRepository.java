package com.gmc.employer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.gmc.employer.model.Employer;

public interface EmployerRepository extends MongoRepository<Employer, String> {

}
