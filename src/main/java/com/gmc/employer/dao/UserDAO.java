package com.gmc.employer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gmc.main.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserDAO {

	@Autowired
	private UserRepository userRepository;

	public void deleteUser(String userId) {
		userRepository.deleteById(userId);
		log.info("User with id: \'{}\' deleted successfully", userId);
	}
}
