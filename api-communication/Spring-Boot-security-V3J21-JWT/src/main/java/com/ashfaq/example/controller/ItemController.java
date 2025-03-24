package com.ashfaq.example.controller;
import com.ashfaq.example.entity.Item;
import com.ashfaq.example.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;




@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> getAllItems(@RequestHeader("Authorization") String authHeader,@RequestHeader("customHeader") String customHeader) {
        System.out.println("customHeader Header: " + customHeader);
        System.out.println("Authorization Header: " + authHeader);
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Optional<Item> getItemById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        System.out.println("Authorization Header: " + authHeader);
        return itemService.getItemById(id);
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemService.saveItem(item);
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        item.setId(id);
        return itemService.saveItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}