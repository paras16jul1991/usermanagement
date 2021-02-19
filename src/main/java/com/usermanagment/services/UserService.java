package com.usermanagment.services;

import com.usermanagment.dao.User;

public interface UserService extends CRUDService<User> {
	User findByUsername(String username);
}