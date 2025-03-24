package com.example.api;

import com.example.api.dto.LoginCreds;
import org.springframework.web.client.RestClient;

public class APIUtils {

    private final static String AUTHURL = "http://localhost:8080/public/authenticate";

    public static String auth(String username, String password) {
        LoginCreds loginCreds = new LoginCreds(username, password);
        return "Bearer " + RestClient.create(AUTHURL).post().body(loginCreds).retrieve().body(String.class).split(":")[1].trim();
    }

}
