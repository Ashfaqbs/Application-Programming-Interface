package com.ashfaq.example.dao;

import com.ashfaq.example.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}