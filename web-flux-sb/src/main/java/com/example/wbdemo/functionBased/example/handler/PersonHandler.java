package com.example.wbdemo.functionBased.example.handler;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.wbdemo.functionBased.example.dto.Person;

import reactor.core.publisher.Mono;

@Service
public class PersonHandler {

    public Mono<ServerResponse> getAllPersons(ServerRequest request) {
        // Logic to retrieve all persons
        return ServerResponse.ok().bodyValue("Retrieve all persons");
    }

    public Mono<ServerResponse> getPersonById(ServerRequest request) {
        String personId = request.pathVariable("id");
        // Logic to retrieve a person by ID
        return ServerResponse.ok().bodyValue("Retrieve person with ID: " + personId);
    }

    public Mono<ServerResponse> createPerson(ServerRequest request) {
        // Logic to create a new person

         return request.bodyToMono(Person.class)
                  .doOnNext(person -> System.out.println("Received Person: " + person))
                  .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> updatePerson(ServerRequest request) {
        String personId = request.pathVariable("id");
        // Logic to update an existing person
        return ServerResponse.ok().bodyValue("Update person with ID: " + personId);
    }

    public Mono<ServerResponse> deletePerson(ServerRequest request) {
        String personId = request.pathVariable("id");
        // Logic to delete a person
        return ServerResponse.ok().bodyValue("Delete person with ID: " + personId);
    }
}
