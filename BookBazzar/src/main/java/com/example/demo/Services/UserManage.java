package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Users;

import com.example.demo.repos.UserReposi;

@Service
public class UserManage {

	@Autowired
	UserReposi userreposi;

	public List<Users> getAll() {
		return userreposi.findAll();
	}

	public Optional<Users> get(int id) {
		return userreposi.findById(id);
	}

	public String update(int id, Users user) {
		Optional<Users> usertemp = userreposi.findById(id);

		if (usertemp.isPresent()) {
			Users use = usertemp.get();

			use.setEmail(user.getEmail());
			use.setName(user.getName());

			userreposi.save(use);

			return "updated successfully 😃";
		}

		return "not found 😭";
	}

	public String delete(int id) {
		Optional<Users> usertemp = userreposi.findById(id);
		if (usertemp.isPresent()) {
			userreposi.deleteById(id);
			return "deleted successfully 😃";
		}

		return "not found 😭 ";
	}

}
