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
    @Query("select b from  Book b where lower(b.name) like :name ")
    Page<Book> findBookByName(@Param("name") String name, Pageable pageable);

    @Query(nativeQuery = true, value = "select b.id, b.isbn, b.name, b.description from book b inner join book_category c on b.id = c.book_id where c.category_id = :category and lower(b.name) like :keyword ORDER BY b.title ASC")
    Page<Book> findBookByCategoryId(@Param("category") Long category, @Param("name") String name, Pageable pageable);

    @Query(nativeQuery = true, value = "select b.id, b.isbn, b.name, b.description from book b inner join book_author au on b.id = au.book_id where au.author_id = :author and lower(b.name) like :name")
    Page<Book> findBookByAuthorId(@Param("author") Long author, @Param("name") String name, Pageable pageable);

}
