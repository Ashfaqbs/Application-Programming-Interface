package com.example.demo.springapiflow_example.service;

import com.example.demo.springapiflow_example.dto.BookCreateRequest;
import com.example.demo.springapiflow_example.dto.BookResponse;
import com.example.demo.springapiflow_example.dto.BookUpdateRequest;
import com.example.demo.springapiflow_example.exception.NotFoundException;
import com.example.demo.springapiflow_example.model.Book;
import com.example.demo.springapiflow_example.repo.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class BookService {
    private final BookRepository repo;

    public BookResponse create(BookCreateRequest r) {
        Book b = Book.builder().title(r.title()).author(r.author()).year(r.year()).build();
        b = repo.save(b);
        return toRes(b);
    }

    public List<BookResponse> list() {
        return repo.findAll().stream().map(this::toRes).toList();
    }

    public BookResponse get(Long id) {
        Book b = repo.findById(id).orElseThrow(() -> new NotFoundException("Book %d not found".formatted(id)));
        return toRes(b);
    }

    public BookResponse update(Long id, BookUpdateRequest r) {
        Book b = repo.findById(id).orElseThrow(() -> new NotFoundException("Book %d not found".formatted(id)));
        b.setTitle(r.title()); b.setAuthor(r.author()); b.setYear(r.year());
        return toRes(repo.save(b));
    }

    public void delete(Long id) {
        Book b = repo.findById(id).orElseThrow(() -> new NotFoundException("Book %d not found".formatted(id)));
        repo.delete(b);
    }

    private BookResponse toRes(Book b) {
        return new BookResponse(b.getId(), b.getTitle(), b.getAuthor(), b.getYear());
    }
}
