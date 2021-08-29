package com.gmc.employer.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmc.employer.dao.EmployerDAO;
import com.gmc.employer.dto.PremiumPoolDTO;
import com.gmc.employer.model.Employer;
import com.gmc.main.jwt.JwtUser;
import com.gmc.main.jwt.JwtValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminService {

	@Autowired
	private JwtValidator jwtValidator;
	@Autowired
	private EmployerDAO employerDAO;

	public Employer addTotalPremiumPool(String token, PremiumPoolDTO premiumPool) {
		log.info("Started to add totalPremiumPool");
		JwtUser jwtUser = jwtValidator.validate(token);
		if (jwtUser == null) {
			log.error("Failed to add the totalPremiumPool");
			return null;
		} else {
			String id = jwtUser.getId();
			Employer employer = employerDAO.findEmployer(id);
			employer.setTotalPremiumPool(premiumPool.getTotalPremiumPool() + employer.getTotalPremiumPool());
			employer.setUpdatedDate(new Date());
			return employerDAO.updateEmployer(employer);
		}
	}
}
