package com.example.service;

import com.example.exception.DatabaseException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.AppUser;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public AppUser getUser(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public List<AppUser> getAllUsers() {
        return repo.findAll();
    }

    public AppUser createUser(AppUser user) {
        try {
            return repo.save(user);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while saving user: " + ex.getMessage());
        }
    }

    public AppUser updateUser(Long id, AppUser userDetails) {
        AppUser user = getUser(id); // throws if not found
        user.setName(userDetails.getName());
        try {
            return repo.save(user);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while updating user: " + ex.getMessage());
        }
    }

    public void deleteUser(Long id) {
        AppUser user = getUser(id); // throws if not found
        try {
            repo.delete(user);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while deleting user: " + ex.getMessage());
        }
    }
}
