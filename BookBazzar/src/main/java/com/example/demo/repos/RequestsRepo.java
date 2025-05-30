package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Requests;

public interface RequestsRepo extends JpaRepository<Requests,Integer> {

}
