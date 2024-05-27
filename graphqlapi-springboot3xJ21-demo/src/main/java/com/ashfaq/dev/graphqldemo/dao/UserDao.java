package com.ashfaq.dev.graphqldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashfaq.dev.graphqldemo.model.User;

public interface UserDao  extends JpaRepository<User, Integer>{

}
