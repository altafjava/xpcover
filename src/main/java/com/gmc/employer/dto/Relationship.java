package com.gmc.employer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {
	private String name;
	private String code;
	@JsonProperty("isMandatory")
	private boolean isMandatory;
}
