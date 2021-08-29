package com.gmc.employer.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Employer {
	private String id;
	private String name;
	private String address;
	private double totalPremiumPool;
	private Date createdDate;
	private Date updatedDate;
}
