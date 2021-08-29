package com.gmc.main.model;

import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document
public class Customer {
	@Id
	private String customerId;
	private String firstName;
	private String lastName;
	private String mobile;
	private boolean isMobileVerified;
	private String email;
	private boolean isEmailVerified;
	private Set<String> roles;
	private boolean isVerifiedCustomer;
	private String password;
}
