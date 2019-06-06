package com.segdoc.service;

import com.segdoc.model.User;

public interface UserService {
	public User findByEmail(String email);
	
	public void saveUser(User user);
}
