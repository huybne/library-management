package com.ensas.librarymanagementsystem.Model;

import com.ensas.librarymanagementsystem.Model.security.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Slf4j
@Table(name = "BORROW")
public class    Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isReturned;
    private LocalDate borrowedAt;
    private LocalDate returnedAt;
    @ManyToOne
    private Book book;
    @ManyToOne
    @JsonIgnore
    private User user;

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public UUID getUserId() {
        return user.getId();
    }
}
