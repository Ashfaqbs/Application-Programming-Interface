package com.ashfaq.concepts.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Payment {
    
    private Long id;

    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be greater than 0")
    private double cost;

    @NotBlank(message = "Product name is mandatory")
    @Size(min = 2, max = 50, message = "Product name must be between 2 and 50 characters")
    private String productName;

    @Size(max = 200, message = "Description must be less than 200 characters")
    private String description;

    @NotBlank(message = "Transaction ID is mandatory")
    private String transactionId;

    @NotNull(message = "Result is mandatory")
    private String result;

  
}


//Summary of Common Validation Annotations
//@NotNull: Ensures the value is not null.
//@NotBlank: Ensures the string is not null and the trimmed length is greater than zero.
//@Size: Ensures the size of the string/collection is within the specified range.
//@Min and @Max: Ensures the numeric value is within the specified range.
//@DecimalMin and @DecimalMax: Ensures the decimal value is within the specified range.
//@Pattern: Ensures the string matches the specified regular expression.