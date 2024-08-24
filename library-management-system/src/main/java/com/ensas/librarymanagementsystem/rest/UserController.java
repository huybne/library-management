package com.ensas.librarymanagementsystem.rest;

import com.ensas.librarymanagementsystem.Model.security.Role;
import com.ensas.librarymanagementsystem.configuration.ActiveUserStore;
import com.ensas.librarymanagementsystem.dto.request.ChangePasswordRequest;
import com.ensas.librarymanagementsystem.dto.request.UserCreationRequest;
import com.ensas.librarymanagementsystem.dto.request.UserUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.UserResponse;
import com.ensas.librarymanagementsystem.repositories.RoleRepository;
import com.ensas.librarymanagementsystem.serviceImpl.UserServiceImpl;
import com.ensas.librarymanagementsystem.util.GeneratePagination;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private ActiveUserStore activeUserStore;

    @Autowired
    private UserServiceImpl userService;

    private final GeneratePagination generatePagination;

    @Autowired
    private RoleRepository roleRepository;

    public UserController(GeneratePagination generatePagination) {
        this.generatePagination = generatePagination;
    }

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

//    @GetMapping
//    public ResponseEntity<?> getBooks(@RequestParam(name = "keyword", defaultValue = "") String keyword,
//                                      @RequestParam(name ="page", defaultValue = "0") int page,
//                                      @RequestParam(name ="size", defaultValue = "5") int size) {
//        Page<BookResponse> bookPage = bookService.getBooks(keyword, page, size);
//        Map<String, Object> response = new HashMap<>();
//        response.put("books", bookPage.getContent());
//        response.put("pagination", generatePagination.pagination(page));
//        response.put("totalPages", bookPage.getTotalPages());
//        response.put("totalElements", bookPage.getTotalElements());
//        response.put("keyword", keyword);
//        response.put("currentSize", size);
//        response.put("currentPage", page);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                           @RequestParam(name ="page", defaultValue = "0") int page,
                                                           @RequestParam(name ="size", defaultValue = "100") int size) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("username : {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        Page<UserResponse> userPage = userService.getAllUsers(keyword, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("users", userPage.getContent());
        response.put("pagination", generatePagination.pagination(page));
        response.put("totalPages", userPage.getTotalPages());
        response.put("totalElements", userPage.getTotalElements());
        response.put("keyword", keyword);
        response.put("currentSize", size);
        response.put("currentPage", page);
        return ResponseEntity.ok(response);

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
@GetMapping("/activeUsers")
public List<String> getActiveUsers() {
    return activeUserStore.users;
}

    @GetMapping("/activeUserCount")
    public int getActiveUserCount() {
        return activeUserStore.users.size();
    }
}
