package com.example.wbdemo.example.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.wbdemo.example.entity.User;
import com.example.wbdemo.example.repo.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    // Retrieve all users with an optional filter by username (using RequestParam)
    @GetMapping
    public Flux<User> getAllUsers(@RequestParam(required = false) String username) {
        if (username != null && !username.isEmpty()) {
            return repository.findByUsername(username);
        }
        return repository.findAll();
    }

    // Retrieve a single user by id with custom header
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable Long id) {
        return repository.findById(id)
                .map(user -> ResponseEntity.ok()
                        .header("Custom-Header", "CustomValue")
                        .body(user))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Create a new user and return 201 Created with Location header
    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@RequestBody User user) {
        return repository.save(user)
                .map(savedUser -> ResponseEntity
                        .created(URI.create("/users/" + savedUser.getId()))
                        .body(savedUser));
    }

    // Update an existing user
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        return repository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setEmail(user.getEmail());
                    return repository.save(existingUser);
                })
                .map(updatedUser -> ResponseEntity.ok(updatedUser))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return repository.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
