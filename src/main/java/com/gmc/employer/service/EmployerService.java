package com.gmc.employer.service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmc.employer.dao.EmployerDAO;
import com.gmc.employer.dto.CreateEmployer;
import com.gmc.employer.dto.UpdateEmployer;
import com.gmc.employer.model.Employer;
import com.gmc.main.enums.Role;
import com.gmc.main.enums.UserType;
import com.gmc.main.jwt.JwtUser;
import com.gmc.main.jwt.JwtValidator;
import com.gmc.main.model.User;
import com.gmc.main.repository.CustomerRepository;
import com.gmc.main.util.PasswordEncryptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployerService {

	@Autowired
	private EmployerDAO employerDAO;
	@Autowired
	private JwtValidator jwtValidator;
	@Autowired
	private CustomerRepository customerRepository;

	public Employer getEmployer(String token, String employerId) {
		JwtUser jwtUser = jwtValidator.validate(token);
		if (jwtUser != null) {
			return employerDAO.findEmployer(employerId);
		}
		return null;
	}

	public List<Employer> getEmployers(String token) {
		JwtUser jwtUser = jwtValidator.validate(token);
		if (jwtUser != null) {
			return employerDAO.findAllEmployers();
		}
		return null;
	}

	public Employer createEmployer(CreateEmployer createEmployerDTO) {
		log.info("started to create employer: {}", createEmployerDTO.getName());
		Employer employer = new Employer();
		BeanUtils.copyProperties(createEmployerDTO, employer);
		Date date = new Date();
		employer.setCreatedDate(date);
		employer.setUpdatedDate(date);
		Employer savedEmployer = employerDAO.saveEmployer(employer);
		String encryptedPassword = PasswordEncryptor.encryptPassword("default");
		User user = new User();
		user.setType(UserType.EMPLOYER.name());
		user.setEmail(createEmployerDTO.getEmail());
		user.setPassword(encryptedPassword);
		Set<String> roles = new LinkedHashSet<>();
		roles.add(Role.EMPLOYER_ADMIN.name());
		user.setRoles(roles);
		user.setId(savedEmployer.getId());
		user.setCreatedDate(date);
		user.setUpdatedDate(date);
		customerRepository.save(user);
		log.info("Employer created with email: {} & temporary password: default", createEmployerDTO.getEmail());
		return savedEmployer;
	}

	public Employer updateEmployer(String token, UpdateEmployer updateEmployerDTO) {
		log.info("started to update employer: {}", updateEmployerDTO.getName());
		JwtUser jwtUser = jwtValidator.validate(token);
		String id = jwtUser.getId();
		Employer employer = employerDAO.findEmployer(id);
		employer.setUpdatedDate(new Date());
		if (updateEmployerDTO.getName() != null) {
			employer.setName(updateEmployerDTO.getName());
		}
		if (updateEmployerDTO.getAddress() != null) {
			employer.setAddress(updateEmployerDTO.getAddress());
		}
		return employerDAO.saveEmployer(employer);
	}

	public void removeEmployer(String token, String employerId) {
		JwtUser jwtUser = jwtValidator.validate(token);
		if (jwtUser != null) {
			employerDAO.deleteEmployer(employerId);
		}
	}
}
