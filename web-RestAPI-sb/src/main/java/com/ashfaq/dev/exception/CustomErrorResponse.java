package com.ashfaq.dev.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {
	private LocalDateTime timestamp;
	private String message;
	private String details;

}
