package com.gmc.employer.service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gmc.employer.dao.EmployerDAO;
import com.gmc.employer.dao.UserDAO;
import com.gmc.employer.dto.CreateEmployer;
import com.gmc.employer.dto.UpdateEmployer;
import com.gmc.employer.model.Employer;
import com.gmc.main.enums.Role;
import com.gmc.main.enums.UserType;
import com.gmc.main.model.User;
import com.gmc.main.repository.UserRepository;
import com.gmc.main.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployerService {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private EmployerDAO employerDAO;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Employer getEmployer(String employerId) {
		return employerDAO.findEmployer(employerId);
	}

	public List<Employer> getEmployers() {
		return employerDAO.findAllEmployers();
	}

	public Employer createEmployer(CreateEmployer createEmployerDTO) {
		log.info("started to create employer: {}", createEmployerDTO.getName());
		Employer employer = new Employer();
		BeanUtils.copyProperties(createEmployerDTO, employer);
		Date date = new Date();
		employer.setCreatedDate(date);
		employer.setUpdatedDate(date);
		Employer savedEmployer = employerDAO.saveEmployer(employer);
		String encryptedPassword = passwordEncoder.encode("default");
		User user = new User();
		user.setType(UserType.EMPLOYER.name());
		user.setEmail(createEmployerDTO.getEmail());
		user.setPassword(encryptedPassword);
		Set<String> roles = new LinkedHashSet<>();
		roles.add(Role.EMPLOYER.name());
		user.setRoles(roles);
		user.setId(savedEmployer.getId());
		user.setCreatedDate(date);
		user.setUpdatedDate(date);
		userRepository.save(user);
		log.info("Employer created with email: {} & temporary password: default", createEmployerDTO.getEmail());
		return savedEmployer;
	}

	public Employer updateEmployer(String employerId, UpdateEmployer updateEmployerDTO) {
		log.info("started to update employer: {}", updateEmployerDTO.getName());
		Employer employer = employerDAO.findEmployer(employerId);
		employer.setUpdatedDate(new Date());
		BeanUtils.copyProperties(updateEmployerDTO, employer, BeanUtil.getNullPropertyNames(updateEmployerDTO));
		return employerDAO.saveEmployer(employer);
	}

	public void removeEmployer(String employerId) {
		employerDAO.deleteEmployer(employerId);
		userDAO.deleteUser(employerId);
	}
}
