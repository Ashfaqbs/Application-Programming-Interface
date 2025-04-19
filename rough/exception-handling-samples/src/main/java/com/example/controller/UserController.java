package com.example.controller;

import com.example.dto.CreateUserRequest;
import com.example.model.AppUser;
import com.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable Long id) {
        AppUser user = service.getUser(id);
        return ResponseEntity.ok(user);
    }

    /*
    {
    "timestamp": "2025-04-19T03:42:42.457836300Z",
    "status": 404,
    "error": "Not Found",
    "message": "User with id 3 not found",
    "path": "/users/3"
}
     */

//
//    @PostMapping
//    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user) {
//        AppUser saved = service.createUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
//    }


//    To enable validation

    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody @Valid CreateUserRequest request) {
        AppUser user = new AppUser();
        user.setName(request.getName());
        AppUser saved = service.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
//    EMpty name or name too long
//    {
//        "name": "1lkndfnskdjnskdjnskdjnlaksmcaojciadjcns;dkcnlzsdjv,kN v,sn vjskdhvslidjbvblsIDjvblasijdfb"
//    }
//    {
//        "timestamp": "2025-04-19T03:53:30.925905500Z",
//            "status": 400,
//            "error": "Validation Failed",
//            "message": "name: Name must be between 3 and 50 characters",
//            "path": "/users"
//    }


    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody AppUser user) {
        AppUser updated = service.updateUser(id, user);
        return ResponseEntity.ok(updated);
    }
    /*
    {
    "timestamp": "2025-04-19T03:44:56.617813400Z",
    "status": 404,
    "error": "Not Found",
    "message": "User with id 3 not found",
    "path": "/users/3"
}
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        System.out.println("Deleting user with id: " + id);
        service.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    /*
    Deleting user with id: 3
Hibernate: select au1_0.id,au1_0.name from app_user au1_0 where au1_0.id=?
ResourceNotFoundException: ErrorResponse{timestamp='2025-04-19T03:42:01.526125600Z', status=404, error='Not Found', message='User with id 3 not found', path='/users/3'}
     */
}
