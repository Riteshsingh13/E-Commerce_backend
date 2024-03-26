package com.captain.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.captain.backend.config.JwtProvider;
import com.captain.backend.exception.UserException;
import com.captain.backend.model.User;
import com.captain.backend.repository.UserRepository;
import com.captain.backend.response.AuthResponse;

@RestController
@RequestMapping("/auth")
public class UserController {

	private UserRepository userRepository;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	private PasswordEncoder passwordEncoder;
	
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
		
		String email = user.getEmail();
		String password = user.getPassword();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		
		User isEmailExist = userRepository.findByEmail(email);
		
		if(isEmailExist != null) {
			throw new UserException("Email is already used with another account. ");
		}else {
			User createdUser = new User();
			createdUser.setEmail(email);
			createdUser.setPassword(passwordEncoder.encode(password));
			createdUser.setFirstName(firstName);
			createdUser.setLastName(lastName);
			
			User savedUser = userRepository.save(createdUser);
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			String token = jwtProvider.generateToken(authentication);
			
			AuthResponse authResponse = new AuthResponse(token, "SignUp success");
			return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
		}
	}
}
