package com.gmc.main.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.gmc.main.model.User;

@Repository
public interface CustomerRepository extends MongoRepository<User, String> {

	User findByEmail(String email);

}
