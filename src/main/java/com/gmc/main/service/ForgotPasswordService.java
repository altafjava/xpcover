package com.gmc.main.service;

import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.gmc.main.jwt.JwtUser;
import com.gmc.main.jwt.JwtService;
import com.gmc.main.model.OTP;
import com.gmc.main.model.User;
import com.gmc.main.repository.OtpRepository;
import com.gmc.main.repository.UserRepository;
import com.gmc.main.util.EncryptorDecryptor;
import com.gmc.main.util.OTPGenerator;
import com.gmc.main.util.PasswordEncryptor;

@Service
public class ForgotPasswordService {

	private static final Logger LOGGER = LogManager.getLogger(SignupService.class);

	@Value("${forgotPasswordSMS}")
	private String forgotPasswordMessage;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private SignupService signupService;
	@Autowired
	private UserRepository customerRepository;
	@Autowired
	private OtpRepository otpRepository;

	public ResponseEntity<String> forgotPassword(String mobile, String email, String otpType) {
		if (isEmailExist(mobile, email)) {
			String generatedOtp = Integer.toString(OTPGenerator.generateOTP());
//			User user = customerRepository.findByEmail(email);
			signupService.saveOTP(generatedOtp, mobile, email, otpType);
			String message = forgotPasswordMessage.replace("{OTP}", generatedOtp);
			LOGGER.info(message);
			boolean flag = false;
			if (mobile == null) {
				// String subject = "Forgot Password OTP";
				LOGGER.info("OTP sending in email " + email);
				// flag = emailUtil.sendMail(email, subject, message);
				flag = true;
			} else {
				LOGGER.info("OTP sending in mobile : " + mobile);
				// Here sent otp in mobile
				// GupshupSMS gupshupSMS = GupshupSMS.getInstance();
				// flag = gupshupSMS.sendSMS(mobile, message, GupshupCredentialType.TRANSACTIONAL.toString());
				flag = true;
			}
			if (flag)
				return new ResponseEntity<>("OTP generated successfully", HttpStatus.OK);
			else
				return new ResponseEntity<>("OTP generation failed", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>("Mobile/Email does not exist", HttpStatus.FORBIDDEN);
	}

	public ResponseEntity<String> verifyOTP(String mobile, String email, String otp, String otpType) {
		String originalOTP = null;
		Optional<User> userOptional = customerRepository.findByEmail(email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			String userId = user.getId();
			List<OTP> otpList = otpRepository.findByOtpTypeAndIsExpiredFalseOrderByUpdatedTimeDesc(otpType);
			if (!otpList.isEmpty()) {
				String encryptedOTP = otpList.get(0).getEncryptedOTP();
				if (encryptedOTP != null) {
					originalOTP = EncryptorDecryptor.decrypt(encryptedOTP);
				} else {
					return new ResponseEntity<>("Mobile OR Email is not valid", HttpStatus.FORBIDDEN);
				}
				if (!otp.equals(originalOTP)) {
					LOGGER.warn("OTP is not valid mobile:" + mobile + "   email:" + email + "  otp:" + otp);
					return new ResponseEntity<>("OTP is not valid", HttpStatus.FORBIDDEN);
				}
				String token = jwtService.createJwtUser(userId, user.getEmail(), user.getRoles());
				LOGGER.info("USER logged in : " + mobile);
				return new ResponseEntity<>(token, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Mobile/Email does not exist", HttpStatus.FORBIDDEN);
	}

	public ResponseEntity<String> resetPassword(String token, String newPassword) {
		JwtUser jwtUser = jwtService.validateJwtToken(token);
		String customerId = jwtUser.getId();
		Optional<User> userOptional = customerRepository.findById(customerId);
		if (userOptional.isPresent()) {
			String encryptedNewPassword = PasswordEncryptor.encryptPassword(newPassword);
			if (encryptedNewPassword != null) {
				User user = userOptional.get();
				user.setPassword(encryptedNewPassword);
				customerRepository.save(user);
				return new ResponseEntity<>("Customer Password updated successfully", HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Password updation failed", HttpStatus.FORBIDDEN);
	}

	private boolean isEmailExist(String mobile, String email) {
		Optional<User> userOptional = customerRepository.findByEmail(email);
		return userOptional.isEmpty() ? false : true;
	}
}
