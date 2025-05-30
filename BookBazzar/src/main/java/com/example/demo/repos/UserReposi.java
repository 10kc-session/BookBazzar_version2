package com.example.demo.repos;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Users;

public interface UserReposi extends JpaRepository<Users,Integer>{
	
	Optional<Users> findByname(String name);
	Boolean existsByname(String name);

}
