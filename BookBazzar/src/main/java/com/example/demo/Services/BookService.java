package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Books;
import com.example.demo.repos.BookRepo;

@Service
public class BookService {
	
	@Autowired
	BookRepo bookrepo;
	
	public List<Books> get(){
		return bookrepo.findAll();
	}
	
	public Optional<Books> getbyid(int id) {
		return bookrepo.findById(id);
	}
	
	public 	Books post(Books book) {
		return bookrepo.save(book);
	}
	
	public String delete(int id) {
		Optional<Books> book = bookrepo.findById(id);
		
		if(book.isPresent()) {
			bookrepo.deleteById(id);
			return "deleted ";
		}
		return "not deleted";
	}
	
	public Optional<Books> update(int id,Books boo) {
		Optional<Books> book = bookrepo.findById(id);
		if(book.isPresent()) {
			Books bk = book.get();
			
			
			bk.setaUthor(boo.getaUthor());
			bk.setDescription(boo.getDescription());
			bk.setImage(boo.getImage());
			bk.setPrice(boo.getPrice());
			bk.setType(boo.getType());
			
			bookrepo.save(bk);
			return book;
		}
		return book;
	}
}
