package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.Enu.Roles;
import com.example.demo.Services.CustomUserDetailService;
import com.example.demo.filters.JwtFilter;

@Configuration
@EnableWebSecurity
public class Secconfig {

	@Autowired
	JwtFilter jwtfilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll().requestMatchers("/user/**")
				.hasRole(Roles.ADMIN.name()) // User controller for ADMIN only
				.requestMatchers("/book/post").hasRole(Roles.ADMIN.name()) // Only ADMIN can POST a book
				.requestMatchers("/book/update/**").hasRole(Roles.ADMIN.name()) // Only ADMIN can UPDATE books
				.requestMatchers("/book/delete/**").hasRole(Roles.ADMIN.name()) // Only ADMIN can DELETE books
				.requestMatchers("/book/getall", "/book/get/**").hasAnyRole(Roles.USER.name(), Roles.ADMIN.name())
				.anyRequest().authenticated()).csrf(csrf -> csrf.disable())
				.addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userdetailsservice() {
		return new CustomUserDetailService();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userdetailsservice,
			PasswordEncoder passwordencoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(userdetailsservice);
		provider.setPasswordEncoder(passwordencoder);

		return new ProviderManager(provider);
	}
}