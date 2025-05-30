package com.example.demo.Entity;

import java.time.LocalDateTime;


import com.example.demo.Enu.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.OneToOne;

@Entity
public class Requests {
	public int getId() {
		return Id;
	}


	public void setId(int id) {
		Id = id;
	}


	public Books getBookid() {
		return bookid;
	}


	public void setBookid(Books bookid) {
		this.bookid = bookid;
	}


	public Users getUserid() {
		return userid;
	}


	public void setUserid(Users userid) {
		this.userid = userid;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public LocalDateTime getDate() {
		return date;
	}


	public void setDate(LocalDateTime date) {
		this.date = date;
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int Id;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="bookid")
	private Books bookid;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="requeste_id")
	private Users userid;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	
	private LocalDateTime date;
	
	
	
}
