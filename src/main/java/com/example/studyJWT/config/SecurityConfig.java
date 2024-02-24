package com.example.studyJWT.config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.example.studyJWT.jwt.JwtAccessDeniedHandler;
import com.example.studyJWT.jwt.JwtAuthenticationEntryPoint;
import com.example.studyJWT.jwt.JwtSecurityConfig;
import com.example.studyJWT.jwt.TokenProvider;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig{
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	public SecurityConfig(
		TokenProvider tokenProvider,
		JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
		JwtAccessDeniedHandler jwtAccessDeniedHandler
	){
		this.tokenProvider = tokenProvider;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
			//토큰 사용하는 방식이므로 csrg는 disable
			.csrf(AbstractHttpConfigurer::disable)

			//익셉션 핸들링
			.exceptionHandling(exceptionHandling
				-> exceptionHandling
					.accessDeniedHandler(jwtAccessDeniedHandler)
					.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			)

			//permitALL
			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.requestMatchers("/api/authenticate","/api/hello","/api/signup", "api/**").permitAll()
				.requestMatchers(PathRequest.toH2Console()).permitAll()
				.anyRequest().authenticated()
			)

			//세션 사용 X
			.sessionManagement(sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			// enable h2-console
			.headers(headers ->
				headers.frameOptions(
					HeadersConfigurer.FrameOptionsConfig::sameOrigin)
			)

			//jwtFilter등록하는 클래스인 jwtSecurityConfig클래스 적용
			.apply(new JwtSecurityConfig(tokenProvider))
		;
		return http.build();
	}
}
