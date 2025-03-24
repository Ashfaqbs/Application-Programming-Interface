package com.example.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


@Service
public class ItemRunnerSync
//        implements CommandLineRunner
{


//    @Override
    public void run(String... args) throws Exception {


//        ADMIN ACC
        try {
//          Get by ID

//            ResponseEntity<Item> objectValue = RestClient
//                    .create("http://localhost:8080/api/items/1")
//                    .get()
//                    .header("Authorization",APIUtils.auth("ashu","ashu"))
//                    .retrieve()
//                    .toEntity(Item.class);
//            System.out.println(objectValue);

//            Get All

            ;

//            List<Item> list = RestClient.create("http://localhost:8080/api/items").get()
//                    .header("Authorization", APIUtils.auth("ashu", "ashu"))
//                    .header("customHeader", "customValue")//excepts a custom header
//                    .retrieve().body(new org.springframework.core.ParameterizedTypeReference<>() {
//                    }); // for List types
//            System.out.println("Size of the list is " + list.size());
//            list.stream().forEach(System.out::println);

//             Post

//            ResponseEntity<String> response = RestClient.create("http://localhost:8080/api/items").post()
//                    .body(new Item("Fan", 1000.00, 1))
//                    .header("Authorization", APIUtils.auth("ashu", "ashu"))
//                    .retrieve().toEntity(String.class);
//            System.out.println("The status code is " + response);

//             Put new Item("Fan", 1000.00, 1) to new Item("Fan", 4000.00, 4 )
//            ResponseEntity<String> fanUpdate = RestClient.create("http://localhost:8080/api/items/18")
//                    .put()
//                    .header("Authorization", APIUtils.auth("ashu", "ashu"))
//                    .body(new Item("Fan", 2000, 5))
//                    .retrieve()
//                    .toEntity(String.class);
//            System.out.println(fanUpdate);


//             Delete
//            ResponseEntity<String> entityDelete = RestClient.create("http://localhost:8080/api/items/17")
//                    .delete()
//                    .header("Authorization", APIUtils.auth("ashu", "ashu"))
//                    .retrieve()
//                    .toEntity(String.class);
//            System.out.println(entityDelete);


        } catch (HttpClientErrorException.BadRequest e) {
            System.out.println("400 - Bad Request: " + e.getMessage());
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("401 - Unauthorized: Invalid credentials.");
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("403 - Forbidden: You don't have access.");
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("404 - Not Found: Resource does not exist.");
        } catch (HttpClientErrorException.Conflict e) {
            System.out.println("409 - Conflict: Duplicate resource.");
        }  catch (HttpClientErrorException e) {
            // Default for any other 4xx or 5xx errors
            System.out.println("HTTP Error " + e.getStatusCode() + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }


    }
}
