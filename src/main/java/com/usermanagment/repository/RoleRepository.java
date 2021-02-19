package com.usermanagment.repository;

import org.springframework.data.repository.CrudRepository;

import com.usermanagment.dao.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}