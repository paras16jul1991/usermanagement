package com.usermanagment.configuration;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		// if (shouldAuthenticateAgainstThirdPartySystem()) {

		// use the credentials
		// and authenticate against the third-party system
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		return new UsernamePasswordAuthenticationToken("user2", "pass", authorities);
	}

	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	

}