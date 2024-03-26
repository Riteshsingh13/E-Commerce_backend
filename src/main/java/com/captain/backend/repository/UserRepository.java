package com.captain.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.captain.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	public User findByEmail(String email);
}
