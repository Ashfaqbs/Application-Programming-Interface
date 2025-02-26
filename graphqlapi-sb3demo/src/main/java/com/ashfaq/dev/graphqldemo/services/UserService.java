package com.ashfaq.dev.graphqldemo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashfaq.dev.graphqldemo.dao.UserDao;
import com.ashfaq.dev.graphqldemo.helper.ExceptionHelper;
import com.ashfaq.dev.graphqldemo.model.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	// Create a new user
	public User createUser(User user) {
		return userDao.save(user);
	}

	// Get all users
	public List<User> getAllUsers() {
		return userDao.findAll();
	}

	// Get user by ID
	public User getUserById(int id) {
		return userDao.findById(id)// helper package
				.orElseThrow(ExceptionHelper::throwResourceNotFoundException);
	}

	// Update a user
	public User updateUser(int id, User userDetails) {
		User user = userDao.findById(id).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
		user.setName(userDetails.getName());
		user.setEmail(userDetails.getEmail());
		user.setPassword(userDetails.getPassword());
		user.setPhoneNO(userDetails.getPhoneNO());
		return userDao.save(user);
	}

	// Delete a user
	public boolean deleteUser(int id) {
		User user = userDao.findById(id).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
		userDao.delete(user);
		return true;
	}
}
