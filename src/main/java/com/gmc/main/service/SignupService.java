package com.gmc.main.service;

import java.util.Date;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.gmc.main.dto.SignupDTO;
import com.gmc.main.dto.SignupReturnDTO;
import com.gmc.main.dto.VerifyOtpDTO;
import com.gmc.main.enums.OtpTypeEnum;
import com.gmc.main.jwt.JwtUtil;
import com.gmc.main.model.OTP;
import com.gmc.main.model.User;
import com.gmc.main.repository.UserRepository;
import com.gmc.main.repository.OtpRepository;
import com.gmc.main.util.OTPEncryptorDecryptor;
import com.gmc.main.util.OTPGenerator;
import com.gmc.main.util.PasswordEncryptor;

@Service
public class SignupService {

	private static final Logger LOGGER = LogManager.getLogger(SignupService.class);

	@Value("${signupSMS}")
	private String signupSMS;
	@Autowired
	private UserRepository customerRepository;
	@Autowired
	private OtpRepository otpRepository;
	@Autowired
	private JwtUtil jwtUtil;

	public ResponseEntity<?> signup(SignupDTO signupDTO) {

		String mobile = signupDTO.getMobile();
		String email = signupDTO.getEmail();
		User user = customerRepository.findByEmail(email);
		if (user == null) {
//			generate otp
			String generatedOtp = Integer.toString(OTPGenerator.generateOTP());
			// save signup details into mongodb
			String customerId = saveSignupDetails(signupDTO, generatedOtp);
//			Here send sms to mobile and email
			String message = signupSMS.replace("{OTP}", generatedOtp);
			LOGGER.info(message);
//			save otp into mongo db
			String otpId = saveOTP(generatedOtp, mobile, email, OtpTypeEnum.SIGNUP.name());
			// return signupId & otpId to the UI
			SignupReturnDTO signupReturnDTO = new SignupReturnDTO();
			signupReturnDTO.setOtpId(otpId);
			signupReturnDTO.setCustomerId(customerId);
			return new ResponseEntity<>(signupReturnDTO, HttpStatus.OK);
		} else {
			String token = jwtUtil.createJwtUser(user.getId(), user.getRoles());
			return new ResponseEntity<>(token, HttpStatus.OK);
		}
	}

	public ResponseEntity<String> verifyOTP(VerifyOtpDTO verifyOtpDTO, String otpType) {
		String userOtp = verifyOtpDTO.getOtp();
		String encryptedOTP = OTPEncryptorDecryptor.encrypt(userOtp);
		OTP otp = otpRepository.findByIdAndOtpTypeAndIsExpiredFalse(verifyOtpDTO.getOtpId(), otpType);
		if (otp != null && otp.getEncryptedOTP().equals(encryptedOTP) && verifyOtpDTO.getMobile().equals(otp.getMobile()) && verifyOtpDTO.getEmail().equals(otp.getEmail())) {
			String id = verifyOtpDTO.getCustomerId();
			Optional<User> userOptional = customerRepository.findById(id);
			if (userOptional.isEmpty()) {
				return new ResponseEntity<>("Wrong customerId", HttpStatus.FORBIDDEN);
			} else {
				User user = userOptional.get();
				String token = jwtUtil.createJwtUser(id, user.getRoles());
				return new ResponseEntity<>(token, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>("Invalid OTP", HttpStatus.FORBIDDEN);
		}
	}

	private String saveSignupDetails(SignupDTO signupDTO, String generatedOtp) {
		User signup = new User();
		BeanUtils.copyProperties(signupDTO, signup);
		String encryptedPassword = PasswordEncryptor.encryptPassword(signupDTO.getPassword());
		signup.setPassword(encryptedPassword);
		signup = customerRepository.save(signup);
		return signup.getId();
	}

	public String saveOTP(String generatedOTP, String mobile, String email, String otpType) {
		String encryptedOTP = OTPEncryptorDecryptor.encrypt(generatedOTP);
		OTP otpDocument = new OTP();
		otpDocument.setCreatedTime(new Date());
		otpDocument.setUpdatedTime(new Date());
		otpDocument.setEncryptedOTP(encryptedOTP);
		otpDocument.setExpired(false);
		otpDocument.setMobile(mobile);
		otpDocument.setEmail(email);
		otpDocument.setOtpType(otpType);
		OTP otpResult = otpRepository.save(otpDocument);
		return otpResult.getId();
	}
}
