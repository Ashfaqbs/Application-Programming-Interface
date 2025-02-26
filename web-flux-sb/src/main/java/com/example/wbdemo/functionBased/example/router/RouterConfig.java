package com.example.wbdemo.functionBased.example.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.wbdemo.functionBased.example.handler.PersonHandler;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(PersonHandler handler) {
        return RouterFunctions.route()
            .GET("/persons", handler::getAllPersons)
            .GET("/persons/{id}", handler::getPersonById)
            .POST("/persons", handler::createPerson)
            .PUT("/persons/{id}", handler::updatePerson)
            .DELETE("/persons/{id}", handler::deletePerson)
            .build();
    }
}
