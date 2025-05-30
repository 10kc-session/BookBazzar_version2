package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Books;

public interface BookRepo extends JpaRepository<Books,Integer> {

}
