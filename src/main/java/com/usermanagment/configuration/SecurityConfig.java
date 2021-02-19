
package com.usermanagment.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.usermanagment.services.SimpleUserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomAuthenticationProvider authenticationProvider;

	@Autowired
	SimpleUserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		auth.authenticationProvider(daoAuthenticationProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/anonymous*")
				.anonymous().antMatchers("/login*").permitAll().antMatchers("/api/register*").permitAll().anyRequest()
				.authenticated().and().formLogin();
		// .loginPage("/login.html").loginProcessingUrl("/perform_login").defaultSuccessUrl("/homepage.html",
		// true);
		// .failureUrl("/login.html?error=true").failureHandler(authenticationFailureHandler()).and().logout()
		// .logoutUrl("/perform_logout").deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler());
	}
}
