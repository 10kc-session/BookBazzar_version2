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
import com.example.demo.Entity.Users;           // Import Users entity
import com.example.demo.Enu.Access;             // Import your Access enum
import com.example.demo.Enu.Status;             // Import your Status enum
import com.example.demo.Services.BookService;
import com.example.demo.Services.UserManage;     // Import your UserManage service (or UserReposi directly)
import com.example.demo.dto.BookRequestDTO;     // Import your BookRequestDTO

@RestController
@RequestMapping("/book")
public class BooksRepo {
	
	@Autowired
	BookService bookservice;
	
	@Autowired
	UserManage userManage; // Inject UserManage to fetch user by ID
	
	// --- GET All Books ---
	@GetMapping("/getall")
	public List<Books> getAll(){
		return bookservice.get();
	}
	
	// --- GET Book by ID ---
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getById(@PathVariable int id ) {
		Optional<Books> opt = bookservice.getbyid(id);
		if(opt.isPresent()) {
			return new ResponseEntity<>(opt.get(),HttpStatus.OK); // Changed from FOUND to OK for successful retrieval
		}
		return new ResponseEntity<>("Book not found with ID: " + id, HttpStatus.NOT_FOUND); // More descriptive error
	}
	
	// --- POST (Add) a New Book ---
	@PostMapping("/post")
	public ResponseEntity<?> post(@RequestBody BookRequestDTO bookDto) { // Now accepts BookRequestDTO
		try {
			Books book = new Books();
			// Map fields from DTO to Entity
			book.setTitle(bookDto.getTitle());
			book.setaUthor(bookDto.getAuthor());
			book.setDescription(bookDto.getDescription());
			book.setPrice(bookDto.getPrice());
			book.setImage(bookDto.getImage());

			// Convert String 'type' from DTO to Access Enum
			if (bookDto.getType() != null && !bookDto.getType().isEmpty()) {
				try {
					book.setType(Access.valueOf(bookDto.getType().toUpperCase()));
				} catch (IllegalArgumentException e) {
					return new ResponseEntity<>("Invalid 'type' value. Must be SELL, DONATE, or EXCHANGE.", HttpStatus.BAD_REQUEST);
				}
			} else {
                 return new ResponseEntity<>("'type' field is required and cannot be empty.", HttpStatus.BAD_REQUEST);
            }

			// Convert String 'status' from DTO to Status Enum
			if (bookDto.getStatus() != null && !bookDto.getStatus().isEmpty()) {
				try {
					book.setStatus(Status.valueOf(bookDto.getStatus().toUpperCase()));
				} catch (IllegalArgumentException e) {
					return new ResponseEntity<>("Invalid 'status' value. Check your Status enum for valid options.", HttpStatus.BAD_REQUEST);
				}
			} else {
                return new ResponseEntity<>("'status' field is required and cannot be empty.", HttpStatus.BAD_REQUEST);
            }

			// Link the book to an existing User based on userId from DTO
			if (bookDto.getUserId() != null) {
				Optional<Users> existingUser = userManage.get(bookDto.getUserId()); // Assuming UserManage.get(id) exists
				if (existingUser.isPresent()) {
					book.setUser(existingUser.get());
				} else {
					return new ResponseEntity<>("User with ID " + bookDto.getUserId() + " not found.", HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<>("User ID ('userId') is required to link the book.", HttpStatus.BAD_REQUEST);
			}

			// Save the book using the service
			Books savedBook = bookservice.post(book);
			return new ResponseEntity<>(savedBook, HttpStatus.CREATED); // Return the saved book with its generated ID

		} catch (Exception e) {
			// Catch any other unexpected exceptions and provide a generic error
			e.printStackTrace(); // Always log the full stack trace for debugging
			return new ResponseEntity<>("An internal server error occurred while adding the book: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// --- PUT (Update) an Existing Book ---
	@PutMapping("/update/{id}")
	public ResponseEntity<?> put(@PathVariable int id,@RequestBody BookRequestDTO bookDto) { // Accepts DTO for update as well
		Optional<Books> opt = bookservice.getbyid(id); // First, find the existing book
		
		if(opt.isPresent()) {
			Books existingBook = opt.get();
			
			// Update fields from DTO to existing entity
			// Only update if the DTO field is not null/empty, to allow partial updates if desired
			if (bookDto.getTitle() != null) existingBook.setTitle(bookDto.getTitle());
			if (bookDto.getAuthor() != null) existingBook.setaUthor(bookDto.getAuthor());
			if (bookDto.getDescription() != null) existingBook.setDescription(bookDto.getDescription());
			if (bookDto.getPrice() > 0) existingBook.setPrice(bookDto.getPrice()); // Assuming price > 0 for update
			if (bookDto.getImage() != null) existingBook.setImage(bookDto.getImage());

			// Update Enum fields with validation
			if (bookDto.getType() != null && !bookDto.getType().isEmpty()) {
				try {
					existingBook.setType(Access.valueOf(bookDto.getType().toUpperCase()));
				} catch (IllegalArgumentException e) {
					return new ResponseEntity<>("Invalid 'type' value for update. Must be SELL, DONATE, or EXCHANGE.", HttpStatus.BAD_REQUEST);
				}
			}
			
			if (bookDto.getStatus() != null && !bookDto.getStatus().isEmpty()) {
				try {
					existingBook.setStatus(Status.valueOf(bookDto.getStatus().toUpperCase()));
				} catch (IllegalArgumentException e) {
					return new ResponseEntity<>("Invalid 'status' value for update. Check your Status enum for valid options.", HttpStatus.BAD_REQUEST);
				}
			}

			// Update User link (if userId is provided in DTO)
			if (bookDto.getUserId() != null) {
				Optional<Users> updatedUser = userManage.get(bookDto.getUserId());
				if (updatedUser.isPresent()) {
					existingBook.setUser(updatedUser.get());
				} else {
					return new ResponseEntity<>("User with ID " + bookDto.getUserId() + " for update not found.", HttpStatus.BAD_REQUEST);
				}
			}
			
			bookservice.post(existingBook); // Use post method (save) to persist updates
			return new ResponseEntity<>(existingBook, HttpStatus.OK); // Return updated book
		}

		return new ResponseEntity<>("Book not found with ID: " + id, HttpStatus.NOT_FOUND);
	}
	
	// --- DELETE a Book ---
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		Optional<Books> opt = bookservice.getbyid(id);
		if(opt.isPresent()) {
			bookservice.delete(opt.get().getId());
			return new ResponseEntity<>("Book with ID: " + id + " deleted successfully.", HttpStatus.OK);
		}
		return new ResponseEntity<>("Book not found with ID: " + id, HttpStatus.NOT_FOUND); // More descriptive error
	}
}