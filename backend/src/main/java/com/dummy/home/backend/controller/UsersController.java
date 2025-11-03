package com.dummy.home.backend.controller;

import com.dummy.home.backend.controller.dtos.CreateUserRequest;
import com.dummy.home.backend.controller.dtos.UpdateUserRequest;
import com.dummy.home.backend.controller.dtos.UserDto;
import com.dummy.home.backend.service.UsersServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersServiceImpl service;

    public UsersController(UsersServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDto> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUser(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateUserRequest req) {
        try {
            UserDto saved = service.create(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest req) {

        UserDto saved = service.update(id, req);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
