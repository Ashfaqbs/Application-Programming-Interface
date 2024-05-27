package com.ashfaq.dev.graphqldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashfaq.dev.graphqldemo.model.Order;

public interface OrderDao extends JpaRepository<Order, Integer>{

}
