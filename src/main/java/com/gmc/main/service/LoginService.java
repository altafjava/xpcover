package com.gmc.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.gmc.main.dto.LoginDTO;
import com.gmc.main.jwt.JwtUtil;
import com.gmc.main.model.User;
import com.gmc.main.util.PasswordEncryptor;

@Service
public class LoginService {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private MongoTemplate mongoTemplate;

	public ResponseEntity<String> login(LoginDTO loginDTO) {
		String hashedPassword = PasswordEncryptor.encryptPassword(loginDTO.getPassword());
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("password").is(hashedPassword), Criteria.where("email").is(loginDTO.getEmail())));

		User user = mongoTemplate.findOne(query, User.class);
		if (user == null) {
			return new ResponseEntity<>("Credential is not valid", HttpStatus.FORBIDDEN);
		} else {
			String token = jwtUtil.createJwtUser(user.getId(), user.getRoles());
			return new ResponseEntity<>(token, HttpStatus.OK);
		}
	}
}
