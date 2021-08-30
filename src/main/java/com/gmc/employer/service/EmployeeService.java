package com.gmc.employer.service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmc.employer.dao.EmployeeDAO;
import com.gmc.employer.dao.UserDAO;
import com.gmc.employer.dto.EmployeeDto;
import com.gmc.employer.model.Employee;
import com.gmc.main.enums.Role;
import com.gmc.main.enums.UserType;
import com.gmc.main.jwt.JwtUser;
import com.gmc.main.jwt.JwtValidator;
import com.gmc.main.model.User;
import com.gmc.main.repository.UserRepository;
import com.gmc.main.util.BeanUtil;
import com.gmc.main.util.PasswordEncryptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeService {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private EmployeeDAO employeeDAO;
	@Autowired
	private JwtValidator jwtValidator;
	@Autowired
	private UserRepository userRepository;

	public Employee getEmployee(String token, String employeeId) {
		jwtValidator.validate(token);
		return employeeDAO.findEmployee(employeeId);
	}

	public List<Employee> getEmployees(String token) {
		JwtUser jwtUser = jwtValidator.validate(token);
		if (jwtUser != null) {
			return employeeDAO.findAllEmployees(jwtUser.getId());
		}
		return null;
	}

	public Employee addEmployee(String token, EmployeeDto employeeDto) {
		log.info("Started to add employee: {}", employeeDto.getName());
		JwtUser jwtUser = jwtValidator.validate(token);
		String employerId = jwtUser.getId();
		Employee employee = new Employee();
		BeanUtils.copyProperties(employeeDto, employee);
		Date date = new Date();
		employee.setCreatedDate(date);
		employee.setUpdatedDate(date);
		employee.setEmployerId(employerId);
		Employee savedEmployee = employeeDAO.saveEmployee(employee);
		String encryptedPassword = PasswordEncryptor.encryptPassword("default");
		User user = new User();
		user.setType(UserType.EMPLOYEE.name());
		user.setEmail(employeeDto.getEmail());
		user.setPassword(encryptedPassword);
		Set<String> roles = new LinkedHashSet<>();
		roles.add(Role.EMPLOYEE.name());
		user.setRoles(roles);
		user.setId(savedEmployee.getId());
		user.setCreatedDate(date);
		user.setUpdatedDate(date);
		userRepository.save(user);
		log.info("Employee added with email: {} & temporary password: default", employeeDto.getEmail());
		return savedEmployee;
	}

	public Employee updateEmployee(String token, EmployeeDto employeeDto, String employeeId) {
		log.info("Started to update the employee");
		jwtValidator.validate(token);
		Employee employee = employeeDAO.findEmployee(employeeId);
		if (employee == null) {
			throw new RuntimeException("No Employee found with the id: " + employeeId);
		} else {
			System.out.println("before Employee " + employee);
			BeanUtils.copyProperties(employeeDto, employee, BeanUtil.getNullPropertyNames(employeeDto));
			System.out.println("After Employee " + employee);
		}
		return employeeDAO.updateEmployee(employee);
	}

	public void removeEmployee(String token, String employeeId) {
		jwtValidator.validate(token);
		employeeDAO.deleteEmployee(employeeId);
		userDAO.deleteUser(employeeId);
	}

}
