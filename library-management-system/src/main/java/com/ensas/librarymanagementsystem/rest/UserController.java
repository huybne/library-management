package com.ensas.librarymanagementsystem.rest;

import com.ensas.librarymanagementsystem.Model.security.Role;
import com.ensas.librarymanagementsystem.dto.request.ChangePasswordRequest;
import com.ensas.librarymanagementsystem.dto.request.UserCreationRequest;
import com.ensas.librarymanagementsystem.dto.request.UserUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.UserResponse;
import com.ensas.librarymanagementsystem.repositories.RoleRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleRepository roleRepository;
    @PostMapping("create")
  //  @PreAuthorize("hasRole('ADMIN')")

    public UserResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        if (request.getRole() == null || request.getRole().isEmpty()) {
            // Gán role "User" mặc định
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            request.setRole(String.valueOf(Collections.singleton(userRole)));
        }
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

    @PutMapping("/{id}/changePassword")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> changePassword(@PathVariable UUID id, @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(id, changePasswordRequest);
            // Trả về chỉ mã trạng thái HTTP 200 OK khi thành công
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Trả về mã trạng thái HTTP 400 BAD REQUEST với thông báo lỗi khi không thành công
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{Id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
