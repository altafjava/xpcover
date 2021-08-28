package com.gmc.employer.service;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmc.employer.dao.EmployerDAO;
import com.gmc.employer.dto.CreateEmployerDTO;
import com.gmc.employer.dto.UpdateEmployerDTO;
import com.gmc.employer.model.Employer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployerService {

	@Autowired
	private EmployerDAO employerDAO;

	public Employer getEmployer(String employerId) {
		return employerDAO.findEmployer(employerId);
	}

	public List<Employer> getEmployers() {
		return employerDAO.findAllEmployers();
	}

	public Employer createEmployer(CreateEmployerDTO employerDTO) {
		log.info("started to create employer: {}", employerDTO.getName());
		Employer employer = new Employer();
		BeanUtils.copyProperties(employerDTO, employer);
		return employerDAO.saveEmployer(employer);
	}

	public Employer updateEmployer(UpdateEmployerDTO employerDTO) {
		log.info("started to update employer: {}", employerDTO.getName());
		Employer updatedEmployer = null;
		if (employerDTO.getId() == null) {
			log.warn("\'id\' is mandatory. Please pass employer id");
		} else {
			Employer employer = new Employer();
			BeanUtils.copyProperties(employerDTO, employer);
			updatedEmployer = employerDAO.saveEmployer(employer);
		}
		return updatedEmployer;
	}

	public void removeEmployer(String employerId) {
		employerDAO.deleteEmployer(employerId);
	}
}
