package com.example.demo.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Books;
import com.example.demo.Services.BookService;


@RestController
@RequestMapping("/book")
public class BooksRepo {
	
	@Autowired
	BookService bookservice;
	
	
	@GetMapping("/getall")
	public List<Books> getAll(){
		return bookservice.get();
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getById(@PathVariable int id ) {
		Optional<Books> opt = bookservice.getbyid(id);
		if(opt.isPresent()) {
			return new ResponseEntity<>(opt.get(),HttpStatus.FOUND);
		}
		return new ResponseEntity<>(opt.get(),HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/post")
	public ResponseEntity<?> post(@RequestBody Books book) {
		 bookservice.post(book);
		 
		 return new ResponseEntity<>(book,HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> put(@PathVariable int id,@RequestBody Books book) {
		Optional<Books> opt  = bookservice.update(id, book);
		if(opt.isPresent()) {
			return new ResponseEntity<>(opt.get(),HttpStatus.CREATED);
		}

		return new ResponseEntity<>("not found",HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		Optional<Books> opt = bookservice.getbyid(id);
		if(opt.isPresent()) {
			bookservice.delete(opt.get().getId());
			return new ResponseEntity<>("Deleted",HttpStatus.OK);
		}
		return new ResponseEntity<>("not Deleted",HttpStatus.NOT_FOUND);
	}
}
