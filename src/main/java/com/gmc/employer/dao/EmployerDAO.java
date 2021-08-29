package com.gmc.employer.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gmc.employer.model.Employer;
import com.gmc.employer.repository.EmployerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EmployerDAO {

	@Autowired
	private EmployerRepository employerRepository;

	public Employer findEmployer(String employerId) {
		log.info("started fetching employer with id: {}", employerId);
		Employer employer = null;
		Optional<Employer> employerOptional = employerRepository.findById(employerId);
		if (employerOptional.isEmpty()) {
			log.info("no employer found with this id: {}", employerId);
		} else {
			employer = employerOptional.get();
		}
		return employer;
	}

	public List<Employer> findAllEmployers() {
		log.info("started fetching list of employers");
		List<Employer> employers = employerRepository.findAll();
		return employers;
	}

	public Employer saveEmployer(Employer employer) {
		Employer employer2 = employerRepository.save(employer);
		log.info("employer \'{}\' saved successfully with id: {}", employer2.getName(), employer2.getId());
		return employer2;
	}

	public Employer updateEmployer(Employer employer) {
		Employer employer2 = employerRepository.save(employer);
		log.info("employer \'{}\' updated successfully", employer2.getName());
		return employer2;
	}

	public void deleteEmployer(String employerId) {
		employerRepository.deleteById(employerId);
		log.info("employer with id: \'{}\' deleted successfully", employerId);
	}
}
