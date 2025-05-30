package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Requests;
import com.example.demo.repos.RequestsRepo;

@Service
public class RequestService {
	
	@Autowired
	RequestsRepo requestsrepo; 
	
	public List<Requests> getall(){
		return requestsrepo.findAll();
	}
	
	public Optional<Requests> get(int id){
		return requestsrepo.findById(id);
	}
	
	public Requests post(Requests requests) {
		return requestsrepo.save(requests);
	}
	
	public String update(int id,Requests requests) {
		Optional<Requests> opt = requestsrepo.findById(id);
		
		if(opt.isPresent()) {
			
			Requests req = opt.get();
			
			req.setDate(requests.getDate());
			req.setStatus(requests.getStatus());
			
			requestsrepo.save(req);
			return "updated";
		}
		
		return "not uodated";
		
	}
	public String delete(int id) {
		Optional<Requests> opt = requestsrepo.findById(id);
		
		if(opt.isPresent()) {
			
			
			requestsrepo.deleteById(id);
			return "deleted successfully";
		}
		
		return "not deleted";
		
	}
	
	
}
