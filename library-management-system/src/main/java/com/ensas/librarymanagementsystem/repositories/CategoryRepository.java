package com.ensas.librarymanagementsystem.repositories;

import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE :name ORDER BY c.name ASC")
    Page<Category> findByName(@Param("name") String name, Pageable pageable);
    @Query("SELECT c FROM Category c ORDER BY c.name ASC")
    List<Category> findAllSortedByName();

}
