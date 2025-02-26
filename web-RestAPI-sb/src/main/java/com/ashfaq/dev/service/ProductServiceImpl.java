package com.ashfaq.dev.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashfaq.dev.exception.InvalidProductIdException;
import com.ashfaq.dev.exception.ProductNotFoundException;
import com.ashfaq.dev.model.Product;
import com.ashfaq.dev.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
    	if (id == null || id <= 0) {
            throw new InvalidProductIdException("Invalid product ID: " + id);
        }
    	Optional<Product> product = productRepository.findById(id);
    	 if (product.isEmpty()) {
             throw new ProductNotFoundException("Product not found with id: " + id);
         }
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    @Override
    public void deleteProduct(Long id) {
    	   if (id == null || id <= 0) {
               throw new InvalidProductIdException("Invalid product ID: " + id);
           }
           if (!productRepository.existsById(id)) {
               throw new ProductNotFoundException("Product not found with id: " + id);
           }
           productRepository.deleteById(id);
    }
    
    @Override
    public Product patchProduct(Long id, Map<String, Object> updates) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        product.setName((String) value);
                        break;
                    case "description":
                        product.setDescription((String) value);
                        break;
                    case "price":
                        product.setPrice((Double) value);
                        break;
                    case "quantity":
                        product.setQuantity((Integer) value);
                        break;
                    case "category":
                        product.setCategory((String) value);
                        break;
                }
            });
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }
    
    
    
}
