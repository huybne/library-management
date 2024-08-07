package com.ensas.librarymanagementsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String telephone;
    private String address;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
    List<String> roles;
}
