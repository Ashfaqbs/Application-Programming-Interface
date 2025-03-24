package com.example.api;

import com.example.api.dto.LoginCreds;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.List;

@SpringBootApplication
public class ApiCallPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCallPracticeApplication.class, args);

//		Rough
//		final String BASE_URL = "https://jsonplaceholder.typicode.com/todos/{id}";
//		ObjectMapper objectMapper = new ObjectMapper();

//		int id = 1;
//		RestClient restClient = RestClient.create();
//		ResponseEntity<User> result =restClient.get().uri(BASE_URL,id)
//				.retrieve().toEntity(User.class);

//		List<User> users = objectMapper.convertValue(response, new TypeReference<List<User>>() {});

//		System.out.println(retrieve);

//		System.out.println("The size of the list is " + retrieve.size());
//		users.stream().limit(10).forEach(System.out::println);
//		System.out.println(result);
//		System.out.println("Contents: " + result.getBody());


//		ADMIN ROLE

//


//		System.out.println(token);eyJhbGciOiJIUzUxMiJ9.eyJpc3N1ZXIgbGluayI6Ind3dy5teXdlYnNpdGUuY29tIiwiaXNzdWVyIG5hbWUiOiJBc2hmYXEiLCJzdWIiOiJhc2h1IiwiaWF0IjoxNzQyNzkwNjYyLCJleHAiOjE3NDI3OTI0NjJ9.AnAjBQQEnuFFUwFijElOxehz_QtOlNNjArHpB3NGnDYVI5DAIDw2EBY2DccPEDcrH9PhVmeB7pMfbVvKwYG2Zw

//final String accessToken="Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3N1ZXIgbGluayI6Ind3dy5teXdlYnNpdGUuY29tIiwiaXNzdWVyIG5hbWUiOiJBc2hmYXEiLCJzdWIiOiJhc2h1IiwiaWF0IjoxNzQyNzkwNjYyLCJleHAiOjE3NDI3OTI0NjJ9.AnAjBQQEnuFFUwFijElOxehz_QtOlNNjArHpB3NGnDYVI5DAIDw2EBY2DccPEDcrH9PhVmeB7pMfbVvKwYG2Zw";
//		String Baseurl="http://localhost:8080/admin/home";
//		RestClient restClient2 = RestClient.create(Baseurl);
//		String content = restClient2.get().header("Authorization",accessToken).retrieve().body(String.class);
//		System.out.println(content); //home_admin

	}




}
