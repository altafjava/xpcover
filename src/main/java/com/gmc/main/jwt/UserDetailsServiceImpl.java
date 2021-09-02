package com.gmc.main.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gmc.main.model.User;
import com.gmc.main.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

//	at the time of login getting owner@gmail.com in (String id)
//	at the time of getEmployers getting 612dfea235010f70f189a248 in (String id)
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(id).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + id));
//		User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));
		return UserDetailsImpl.build(user);
	}
}
