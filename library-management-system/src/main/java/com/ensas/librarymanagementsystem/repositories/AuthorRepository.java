package com.ensas.librarymanagementsystem.repositories;

import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ensas.librarymanagementsystem.Model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("select a from Author a where lower(a.name) like :name order by a.name ASC ")
    Page<Author> findByName(@Param("name") String name, Pageable pageable);
    @Query("SELECT a FROM Author a ORDER BY a.name ASC")
    List<Author> findAllSortedByName();
}
