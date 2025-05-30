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

import com.example.demo.Entity.Requests;
import com.example.demo.Services.RequestService;

@RestController
@RequestMapping("/req")
public class RequestReposit {
	
	@Autowired
	RequestService requestservice;
	
	@GetMapping("/getall")
	public List<Requests> getall(){
		return requestservice.getall(); 
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> get(@PathVariable int id){
		Optional<Requests> opt = requestservice.get(id);
		
		if(opt.isPresent()) {
			return new ResponseEntity<>(opt.get(),HttpStatus.OK);
		}
		
		return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
		}
		
	
	@PostMapping("/post")
	public ResponseEntity<?> post(@RequestBody Requests request) {
		return new ResponseEntity<>(requestservice.post(request),HttpStatus.CREATED);
	}
	
	@PutMapping("/updtae/{id}")
	public ResponseEntity<?> put(@RequestBody Requests request,@PathVariable int id) {
		Optional<Requests> opt = requestservice.get(id);
		
		if(opt.isPresent()) {
			requestservice.update(opt.get().getId(),request);
			return new ResponseEntity<>("updated",HttpStatus.ACCEPTED);
			
		}
		return new ResponseEntity<>("not updated",HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/del/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
Optional<Requests> opt = requestservice.get(id);
		
		if(opt.isPresent()) {
			requestservice.delete(opt.get().getId());
			return new ResponseEntity<>("deleted",HttpStatus.ACCEPTED);
			
		}
		return new ResponseEntity<>("not deleted",HttpStatus.NOT_FOUND);
	}
}
