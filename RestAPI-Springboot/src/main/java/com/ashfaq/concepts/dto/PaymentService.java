package com.ashfaq.concepts.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private List<Payment> payments = new ArrayList<>(Arrays.asList(
        new Payment(1L, 100.00, "Product1", "Description1", "TXN12345", "Success"),
        new Payment(2L, 200.00, "Product2", "Description2", "TXN12346", "Success"),
        new Payment(3L, 300.00, "Product3", "Description3", "TXN12347", "Success"),
        new Payment(4L, 400.00, "Product4", "Description4", "TXN12348", "Success"),
        new Payment(5L, 500.00, "Product5", "Description5", "TXN12349", "Success"),
        new Payment(6L, 600.00, "Product6", "Description6", "TXN12350", "Success"),
        new Payment(7L, 700.00, "Product7", "Description7", "TXN12351", "Success"),
        new Payment(8L, 800.00, "Product8", "Description8", "TXN12352", "Success"),
        new Payment(9L, 900.00, "Product9", "Description9", "TXN12353", "Success"),
        new Payment(10L, 1000.00, "Product10", "Description10", "TXN12354", "Success")
    ));

    public PaymentDTO convertToDTO(Payment payment) {
        return new PaymentDTO(payment.getId(),payment.getCost(), payment.getProductName(), payment.getTransactionId());
    }

    public List<PaymentDTO> getAllPayments() {
        return payments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Payment createPayment(Payment payment) {
        payment.setId((long) (payments.size() + 1)); // Assign an ID
        payments.add(payment);
        return payment;
    }
}
