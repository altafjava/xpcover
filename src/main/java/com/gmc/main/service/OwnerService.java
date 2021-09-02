package com.gmc.main.service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gmc.main.enums.Role;
import com.gmc.main.enums.UserType;
import com.gmc.main.model.User;
import com.gmc.main.repository.UserRepository;

@Service
public class OwnerService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void createOwner() {
		String email = "owner@gmail.com";
		Optional<User> optional = userRepository.findByEmail(email);
		if (optional.isEmpty()) {
			User user = new User();
			user.setType(UserType.OWNER.name());
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode("default"));
			Set<String> roles = new LinkedHashSet<>();
			roles.add(Role.OWNER.name());
			user.setRoles(roles);
			Date date = new Date();
			user.setCreatedDate(date);
			user.setUpdatedDate(date);
			userRepository.save(user);
		}
	}
}
