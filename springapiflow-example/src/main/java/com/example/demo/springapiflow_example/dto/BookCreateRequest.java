package com.example.demo.springapiflow_example.dto;

import jakarta.validation.constraints.*;

public record BookCreateRequest(
        @NotBlank @Size(max=120) String title,
        @NotBlank @Size(max=80)  String author,
        @NotNull @Min(1400) @Max(2100) Integer year
) {}
