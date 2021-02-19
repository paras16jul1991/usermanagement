package com.usermanagment.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usermanagment.dao.RegisterUser;
import com.usermanagment.dao.Role;
import com.usermanagment.dao.User;
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

	// @Secured("ROLE_USER")
	// @RolesAllowed("ROLE_USER")
	@GetMapping("/view")
	public User viewUser() {

		return repo.findByUsername("user1");
	}

	@Transactional(rollbackFor = AlreadyExistException.class, propagation=Propagation.NEVER)
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
