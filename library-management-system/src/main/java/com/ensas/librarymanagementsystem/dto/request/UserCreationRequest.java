package com.ensas.librarymanagementsystem.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String telephone;
    private String address;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
}
