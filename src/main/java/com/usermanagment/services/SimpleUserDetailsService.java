package com.usermanagment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.usermanagment.dao.User;

@Service
public class SimpleUserDetailsService implements UserDetailsService {

	private UserService userService;
	private Converter<User, UserDetails> userUserDetailsConverter;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	@Qualifier(value = "userToUserDetails")
	public void setUserUserDetailsConverter(Converter<User, UserDetails> userUserDetailsConverter) {
		this.userUserDetailsConverter = userUserDetailsConverter;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetails user = userUserDetailsConverter.convert(userService.findByUsername(username));
		if (user == null) {
			throw new UsernameNotFoundException(String.format("Username not found for domain, username=%s", username));
		}
		return user;
	}

}