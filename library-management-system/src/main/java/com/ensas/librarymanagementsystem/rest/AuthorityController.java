package com.ensas.librarymanagementsystem.rest;

import com.ensas.librarymanagementsystem.dto.request.AuthorityRequest;
import com.ensas.librarymanagementsystem.dto.response.AuthorityResponse;
import com.ensas.librarymanagementsystem.service.AuthorityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authorities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthorityController {
    @Autowired
    AuthorityService authorityService;

    @PostMapping
    public ResponseEntity<AuthorityResponse> create(@RequestBody AuthorityRequest request) {
        try {
            AuthorityResponse response = authorityService.create(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating authority: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get all authorities.
     * @return the list of all authorities.
     */
    @GetMapping
    public ResponseEntity<List<AuthorityResponse>> getAll() {
        try {
            List<AuthorityResponse> response = authorityService.getAll();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching authorities: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Delete an authority by ID.
     * @param id the ID of the authority to be deleted.
     * @return a response entity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        try {
            authorityService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting authority with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthority(@PathVariable("id") UUID id,@RequestBody AuthorityRequest request) {
        try{
            authorityService.updateAuthority(id, request);
            return ResponseEntity.ok("Authority updated successfully.");
        }catch (Exception e) {
            log.error("Error update role with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<AuthorityResponse> getAuthorityByName(@PathVariable("name") String name) {
        try{
            AuthorityResponse authority = authorityService.getAuthorityByName(name);
            return ResponseEntity.ok(authority);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
