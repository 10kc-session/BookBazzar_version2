package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Users;
import com.example.demo.Services.UserManage;

@RestController
@RequestMapping("/user")
public class UserControll {
	
	@Autowired
	UserManage usermanage;
	
	
	@GetMapping("/getall")
	public List<Users> getAll(){
		return usermanage.getAll();
	}
	
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> get(@PathVariable int id){
		Optional<Users> opt = usermanage.get(id);
		if(opt.isPresent()) {
			return new ResponseEntity<>(opt.get(),HttpStatus.FOUND);
		}
		
		return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
	}
	
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable int id,@RequestBody Users users) {
		Optional<Users> opt = usermanage.get(id);
		
		if(opt.isPresent()) {
			 usermanage.update(id, users);
			 return new ResponseEntity<>("updated successfully",HttpStatus.ACCEPTED);
		}
		 return new ResponseEntity<>("not updated",HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		Optional<Users> opt = usermanage.get(id);
		
		if(opt.isPresent()) {
			 usermanage.delete(id);
			 return new ResponseEntity<>("deleted successfully",HttpStatus.ACCEPTED);
		}
		 return new ResponseEntity<>("not DELETED",HttpStatus.NOT_FOUND);
	}
}

