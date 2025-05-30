package com.example.demo.Services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Users;
import com.example.demo.repos.UserReposi;


@Service
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	UserReposi userepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userepo.findByname(username).orElseThrow(()->new  UsernameNotFoundException("user not found"));
		
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),user.getPassword(),user.getAuthorities()
		);
}}
