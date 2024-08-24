package com.ensas.librarymanagementsystem.rest;

import com.ensas.librarymanagementsystem.dto.request.AddAuthoritiesToRole;
import com.ensas.librarymanagementsystem.dto.request.AddRoleToUser;
import com.ensas.librarymanagementsystem.dto.request.RoleRequest;
import com.ensas.librarymanagementsystem.dto.response.RoleResponse;
import com.ensas.librarymanagementsystem.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> create(@RequestBody RoleRequest request) {
        try {
            RoleResponse response = roleService.create(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating role: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAll() {
        try {
            List<RoleResponse> response = roleService.getAll();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching role: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> delete(@PathVariable UUID roleId) {
        try {
            roleService.delete(roleId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting role with id {}: {}", roleId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/addAuthorities")
    public ResponseEntity<?> addAuthoritiesToRole(@RequestBody AddAuthoritiesToRole request) {
        roleService.addAuthorityToRole(request);
        return ResponseEntity.ok("Authorities added to role successfully.");
    }
    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable("roleId") UUID id,@RequestBody RoleRequest roleRequest) {
        try{
            RoleResponse response   =  roleService.updateRole(id, roleRequest);
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            log.error("Error update role with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleResponse> getRoleByName(@PathVariable String roleName) {
        try {
            RoleResponse response = roleService.getRoleByName(roleName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching role by name {}: {}", roleName, e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("grant-role")
    public ResponseEntity<?> grantRole(@RequestBody AddRoleToUser roleRequest) {
        try {
            RoleResponse response = roleService.addRoleToUser(roleRequest);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            log.error("Error adding role to user: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
