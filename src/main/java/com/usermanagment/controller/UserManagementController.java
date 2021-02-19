package com.usermanagment.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usermanagment.dao.RegisterUser;
import com.usermanagment.dao.Role;
import com.usermanagment.dao.User;
import com.usermanagment.dao.UserResponse;
import com.usermanagment.exception.AlreadyExistException;
import com.usermanagment.repository.RegisterUserRepository;
import com.usermanagment.repository.RoleRepository;
import com.usermanagment.repository.UserRepository;

@RestController
@RequestMapping("/api/")
public class UserManagementController {

	@Autowired
	UserRepository repo;
	@Autowired
	RoleRepository roleRepo;
	@Autowired
	RegisterUserRepository regiRepo;

	//Database should has ROLE_ADMIN as role in DB
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/user/view")
	public UserResponse viewUser(@RequestParam("userName") String userName) {

		User user = repo.findByUsername(userName);

		UserResponse userResponse = new UserResponse();
		userResponse.setUserName(userName);
		userResponse.setEmail(user.getEmail());
		List<Role> roles = user.getRoles();
		roles.stream().forEach(role -> userResponse.getRole().add(role.getRole()));

		return userResponse;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/user/view/all")
	public List<UserResponse> viewAllUser() {
		List<UserResponse> userResponses = new ArrayList<>();
		Iterable<User> users = repo.findAll();
		users.forEach(user -> {
			UserResponse userResponse = new UserResponse();
			userResponse.setUserName(user.getUsername());
			userResponse.setEmail(user.getEmail());
			List<Role> roles = user.getRoles();
			roles.stream().forEach(role -> userResponse.getRole().add(role.getRole()));
			userResponses.add(userResponse);
		});
		return userResponses;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/role/save")
	public String createRole(@RequestBody Role role) throws AlreadyExistException {
		roleRepo.save(role);
		return "Success";
	}

	@Transactional(rollbackFor = AlreadyExistException.class, propagation = Propagation.REQUIRED)
	@PostMapping("/register")
	public String createUser(@RequestBody RegisterUser registerUser) throws AlreadyExistException {

		createEntryInRegisterUserTable(registerUser);

		if (repo.findByUsername(registerUser.getUsername()) != null) {
			throw new AlreadyExistException("Failure : User already registered");
		}
		User user = new User();
		user.setUsername(registerUser.getUsername());
		user.setEncryptedPassword(new BCryptPasswordEncoder().encode(registerUser.getPassword()));
		user.setEnabled(true);
		user.setEmail(registerUser.getEmail());
		ArrayList<Role> arrayList = new ArrayList<Role>();
		arrayList.add(roleRepo.findById(1).get());
		user.setRoles(arrayList);
		repo.save(user);

		registerUser.setIsRegisterationCompleted("Y");
		regiRepo.save(registerUser);

		return "Success";
	}

	private void createEntryInRegisterUserTable(RegisterUser registerUser) {
		registerUser.setIsRegisterationCompleted("N");
		regiRepo.save(registerUser);
	}

	@ExceptionHandler(AlreadyExistException.class)
	ResponseEntity<String> handler(AlreadyExistException e) {
		ResponseEntity<String> response = new ResponseEntity<String>(e.getMessage() + ", try with Other Username",
				HttpStatus.BAD_REQUEST);
		return response;
	}
}
