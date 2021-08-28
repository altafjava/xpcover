package com.gmc.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.gmc.main.dto.SignupDTO;
import com.gmc.main.dto.VerifyOtpDTO;
import com.gmc.main.enums.OtpTypeEnum;
import com.gmc.main.service.SignupService;

@RestController
public class SignupController {

	@Autowired
	private SignupService signupService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupDTO signupDTO) {
		return signupService.signup(signupDTO);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<String> verifyOTP(@RequestBody VerifyOtpDTO verifyOtpDTO) {
		return signupService.verifyOTP(verifyOtpDTO, OtpTypeEnum.SIGNUP.name());
	}
}
