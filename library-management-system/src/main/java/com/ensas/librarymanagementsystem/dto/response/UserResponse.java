    package com.ensas.librarymanagementsystem.dto.response;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;
    import java.util.Set;
    import java.util.UUID;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserResponse {
        UUID id;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String telephone;
        private String address;
        Set<RoleResponse> roles;
        private LocalDate createdDate;
        private LocalDate lastModifiedDate;
    }
