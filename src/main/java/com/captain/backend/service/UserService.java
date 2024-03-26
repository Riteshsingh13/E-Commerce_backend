package com.captain.backend.service;

import org.springframework.stereotype.Service;

import com.captain.backend.exception.UserException;
import com.captain.backend.model.User;

@Service
public interface UserService {

	public User findUserById(Long userId)throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
}
