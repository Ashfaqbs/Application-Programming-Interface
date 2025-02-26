package com.example.wbdemo.example.repo;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.example.wbdemo.example.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findByEmail(String email);

        Flux<User> findByUsername(String username);

}