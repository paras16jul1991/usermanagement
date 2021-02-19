package com.usermanagment.repository;

import org.springframework.data.repository.CrudRepository;

import com.usermanagment.dao.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	User findByUsername(String username);
}
