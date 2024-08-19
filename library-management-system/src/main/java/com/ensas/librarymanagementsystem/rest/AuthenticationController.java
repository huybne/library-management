package com.ensas.librarymanagementsystem.rest;

import com.ensas.librarymanagementsystem.dto.request.AuthenticationRequest;
import com.ensas.librarymanagementsystem.dto.request.LogoutRequest;
import com.ensas.librarymanagementsystem.dto.request.RefreshRequest;
import com.ensas.librarymanagementsystem.dto.request.VerifyRequest;
import com.ensas.librarymanagementsystem.dto.response.AuthenticationResponse;
import com.ensas.librarymanagementsystem.dto.response.VerifyResponse;
import com.ensas.librarymanagementsystem.exceptions.InvalidPassword;
import com.ensas.librarymanagementsystem.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    final AuthenticationService authenticationService;


    @PostMapping(value ="/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            log.error("Username not found", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username not found");
        } catch (InvalidPassword e) {
            log.error("Invalid password", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        } catch (Exception e) {
            log.error("Authentication failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed");
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyRequest request, Model model) {
        try {
            VerifyResponse response = authenticationService.verify(request);
            return ResponseEntity.ok(response);
        } catch (ParseException | JOSEException e) {
            log.error("Token verification failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token verification failed");
        } catch (Exception e) {
            log.error("Unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> authenticate(@RequestBody RefreshRequest request) {
        try {
            var result = authenticationService.refreshToken(request);
            return ResponseEntity.ok(result);
        } catch (UsernameNotFoundException e) {
            log.error("Username not found", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username not found");
        } catch (InvalidPassword e) {
            log.error("Invalid password", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        } catch (Exception e) {
            log.error("refresh failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Refresh failed");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
