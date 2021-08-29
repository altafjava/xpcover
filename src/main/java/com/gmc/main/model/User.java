package com.gmc.main.model;

import java.util.Date;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
	private String id;
	private String type;
	private String email;
	private String password;
	private Set<String> roles;
	private Date createdDate;
	private Date updatedDate;
}
