package com.ensas.librarymanagementsystem.repositories;

import com.ensas.librarymanagementsystem.Model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface  UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    void deleteById(UUID id);
    boolean existsByUsername(String username);

}
