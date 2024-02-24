package com.example.studyJWT.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.studyJWT.data.User;

public interface UserRepository extends JpaRepository<User, Long> {
		@EntityGraph(attributePaths = "authorities")
		Optional<User> findOneWithAuthoritiesByUsername(String username);
}
