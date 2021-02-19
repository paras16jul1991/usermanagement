package com.usermanagment.repository;

import org.springframework.data.repository.CrudRepository;

import com.usermanagment.dao.RegisterUser;

public interface RegisterUserRepository extends CrudRepository<RegisterUser, Integer> {
}
