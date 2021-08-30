package com.gmc.employer.dto;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeDto {
	private String name;
	private String gender;
	private Date dateOfBirth;
	private String email;
	private String phone;
	private String address;
	private String designation;
	private Date joiningDate;
	private String coverageType;
	private String coverageAmount;
	private List<Member> members;
}
