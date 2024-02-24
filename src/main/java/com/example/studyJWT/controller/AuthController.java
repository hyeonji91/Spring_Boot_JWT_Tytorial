package com.example.studyJWT.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studyJWT.data.dto.AuthRequest;
import com.example.studyJWT.data.dto.AuthResponse;
import com.example.studyJWT.jwt.JwtFilter;
import com.example.studyJWT.jwt.TokenProvider;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse.TokenDto> authorize(@Valid @RequestBody AuthRequest.LoginDto loginDto){
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.createToken(authentication);

		HttpHeaders headers = new HttpHeaders();
		headers.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer "+jwt);

		return new ResponseEntity<>(new AuthResponse.TokenDto(jwt), headers, HttpStatus.OK);
	}



}
