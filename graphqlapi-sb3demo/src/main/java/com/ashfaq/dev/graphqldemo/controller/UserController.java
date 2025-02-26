package com.ashfaq.dev.graphqldemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ashfaq.dev.graphqldemo.model.User;
import com.ashfaq.dev.graphqldemo.services.UserService;

@RestController
//or @Controller
public class UserController {

	@Autowired
	private UserService userService;

	@MutationMapping // automatic mapping to the graphql file USER mutation (any function which will
						// modify or creates) function
	// name must be same else we have to provide arg in the annotation name=""
	public User createUser(@Argument String name, @Argument String phoneNO, @Argument String email,
			@Argument String password) {

		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setPhoneNO(phoneNO);

		return userService.createUser(user);
	}

	@QueryMapping(name = "getUsers")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@QueryMapping(name = "getUser")
	public User getUserById(@Argument("userID") String idStr) {
	    System.out.println("Received ID: " + idStr);
	    int id = Integer.parseInt(idStr); // Convert String to int
	    User user = userService.getUserById(id);
	    return user;
	}


	@MutationMapping
	public boolean deleteUser(@Argument("userID") String idStr) {
		
		int id = Integer.parseInt(idStr);
		boolean deleteUser = userService.deleteUser(id);
		return deleteUser;
	}

	public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User userDetails) {
		User updatedUser = userService.updateUser(id, userDetails);
		return ResponseEntity.ok(updatedUser);
	}

}
