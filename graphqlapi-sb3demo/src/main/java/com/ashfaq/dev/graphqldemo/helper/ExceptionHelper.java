package com.ashfaq.dev.graphqldemo.helper;

public class ExceptionHelper {

	public static RuntimeException throwResourceNotFoundException() {
		return new RuntimeException("Resource Not Found ");
	}
}
