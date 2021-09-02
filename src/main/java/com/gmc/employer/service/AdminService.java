package com.gmc.employer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmc.employer.dao.EmployerDAO;
import com.gmc.employer.dto.PolicyDto;
import com.gmc.employer.dto.Premium;
import com.gmc.employer.dto.Relationship;
import com.gmc.employer.dto.RelationshipOption;
import com.gmc.employer.model.Employer;
import com.gmc.employer.model.Policy;
import com.gmc.main.enums.Relation;
import com.gmc.main.jwt.JwtService;
import com.gmc.main.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminService {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private EmployerDAO employerDAO;

	public Employer addTotalPremiumPool(String token, Premium premiumPool) {
		log.info("Started to add totalPremiumPool");
		String employerId = jwtService.getIdFromToken(token);
		Employer employer = employerDAO.findEmployer(employerId);
		employer.setTotalPremiumPool(premiumPool.getTotalPremiumPool() + employer.getTotalPremiumPool());
		employer.setUpdatedDate(new Date());
		return employerDAO.updateEmployer(employer);
	}

	public Employer addPolicy(String token, PolicyDto policyDto) {
		String employerId = jwtService.getIdFromToken(token);
		Policy policy = new Policy();
		policy.setName(policyDto.getName());
		Date startDate = new Date();
		policy.setStartDate(startDate);
		policy.setEndDate(DateUtil.getLastDateOfYear(startDate));
		Employer employer = employerDAO.findEmployer(employerId);
		employer.setPolicy(policy);
		employerDAO.updateEmployer(employer);
		return employer;
	}

	public List<Relationship> getAllowedRelationships() {
		List<Relationship> relationships = new ArrayList<>();
		Relationship relationship = new Relationship("Self", Relation.SELF.name(), true);
		relationships.add(relationship);
		relationship = new Relationship("Spouse", Relation.SPOUSE.name(), true);
		relationships.add(relationship);
		relationship = new Relationship("Child", Relation.CHILD.name(), false);
		relationships.add(relationship);
		relationship = new Relationship("Father", Relation.FATHER.name(), false);
		relationships.add(relationship);
		relationship = new Relationship("Mother", Relation.MOTHER.name(), false);
		relationships.add(relationship);
		relationship = new Relationship("Father-in-law", Relation.FIL.name(), false);
		relationships.add(relationship);
		relationship = new Relationship("Mother-in-law", Relation.MIL.name(), false);
		relationships.add(relationship);
		return relationships;
	}

	public RelationshipOption getRelationshipOptions() {
		RelationshipOption relationshipOption = new RelationshipOption();
		List<String> options = new ArrayList<>();
		options.add("Employee");
		options.add("Employee + 1");
		options.add("Employee + 2");
		options.add("Employee + 3");
		options.add("Employee + 4");
		options.add("Employee + 5");
		options.add("Employee + 6");
		relationshipOption.setOptions(options);
		relationshipOption.setMaxAllowedMember(options.size());
		return relationshipOption;
	}

}
