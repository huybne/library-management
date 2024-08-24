package com.ensas.librarymanagementsystem.repositories;

import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Borrow;
import com.ensas.librarymanagementsystem.Model.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface  UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    void deleteById(UUID id);
    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE :username ORDER BY u.username ASC")
    Page<User> findAllSort(@Param("username") String name, Pageable pageable);

//    @Query("select U from User U where  LOWER(u.username) like :username ORDER BY u.username ASC")
//    Page<User> findAllUserBorrowed(@Param("username") String name, Pageable pageable);

    List<User> findAllByOrderByUsernameAsc(Pageable pageable);

}
