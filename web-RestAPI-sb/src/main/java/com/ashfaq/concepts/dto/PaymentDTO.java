package com.ashfaq.concepts.dto;

public record PaymentDTO(Long id, double cost, String productName, String transactionId) {
}
