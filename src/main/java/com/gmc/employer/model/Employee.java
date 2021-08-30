package com.gmc.employer.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Employee {
	private String id;
	private String employerId;
	private String name;
	private String gender;
	private Date dateOfBirth;
	private String email;
	private String phone;
	private String designation;
	private String address;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private Date joiningDate;
	private String coverageType;
	private String coverageAmount;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private Date createdDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private Date updatedDate;
}
