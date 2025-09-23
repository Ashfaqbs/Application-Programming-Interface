package com.example.demo.springapiflow_example.repo;

import com.example.demo.springapiflow_example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long> {}
