package com.ensas.librarymanagementsystem.rest;

import com.ensas.librarymanagementsystem.Model.security.Role;
import com.ensas.librarymanagementsystem.Model.security.User;
import com.ensas.librarymanagementsystem.dto.request.UserCreationRequest;
import com.ensas.librarymanagementsystem.dto.request.UserUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.UserResponse;
import com.ensas.librarymanagementsystem.serviceImpl.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserServiceImpl userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        return userService.createUser(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<UserResponse> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("username : {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public UserResponse getUser(@PathVariable("id") UUID userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{Id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(@PathVariable("Id") UUID userId, @RequestBody UserUpdateRequest request) {
        return userService.UpdateUser(userId, request);
    }

    @DeleteMapping("/{Id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable("Id") UUID userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/myInfo")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public UserResponse getMyInfo() {
        return userService.getMyInfo();
    }
//    @PutMapping("/{userId}/roles")
//    public ResponseEntity<String> addRolesToUser(@PathVariable("userId") UUID userId, @RequestBody List<Role> roleNames) {
//        try {
//            userService.addRolesToUser(userId, roleNames);
//            return ResponseEntity.ok("Roles added successfully to the user.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add roles to the user: " + e.getMessage());
//        }
//    }
}
