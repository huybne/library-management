package com.ensas.librarymanagementsystem.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ensas.librarymanagementsystem.Model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE LOWER(b.name) LIKE :name ORDER BY b.name ASC")
    Page<Book> findBookByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :category AND LOWER(b.name) LIKE :name ORDER BY b.name ASC")
    Page<Book> findBookByCategoryId(@Param("category") Long category, @Param("name") String name, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :author AND LOWER(b.name) LIKE :name ORDER BY b.name ASC")
    Page<Book> findBookByAuthorId(@Param("author") Long author, @Param("name") String name, Pageable pageable);
}


