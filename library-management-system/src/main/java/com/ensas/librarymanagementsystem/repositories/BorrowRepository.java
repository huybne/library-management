package com.ensas.librarymanagementsystem.repositories;

import com.ensas.librarymanagementsystem.Model.Borrow;
import com.ensas.librarymanagementsystem.dto.response.AllBorrowedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    @Query("SELECT b FROM Borrow b WHERE b.book.id = :bookId AND b.user.id = :userId AND b.isReturned = false ORDER BY b.borrowedAt ASC ")
    Optional<Borrow> findAlreadyBorrowed(@Param("bookId") Long bookId, @Param("userId") UUID userId);


    @Query("select b from Borrow  b where b.book.id = :bookId and b.user.id = :userId ORDER BY b.borrowedAt ASC ")
    Optional<Borrow> findReturnedBorrowed(@Param("bookId") Long bookId, @Param("userId") UUID userId);


    @Query("select  b from Borrow  b where b.user.id =:userId and lower(b.book.name) like :name and b.isReturned = false ORDER BY b.borrowedAt ASC ")
    Page<Borrow> findBorrowedBooks(@Param("userId") UUID userId, @Param("name") String name, Pageable pageable);



    @Query("SELECT b FROM Borrow b WHERE b.isReturned = false AND (LOWER(b.book.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.book.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))) ORDER BY b.borrowedAt DESC")
    Page<Borrow> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Borrow b WHERE  b.isReturned = false ORDER BY b.borrowedAt DESC")
    Optional<Borrow> findAllGroup();

//    @Query("SELECT new com.ensas.librarymanagementsystem.dto.response.AllBorrowedResponse(" +
//            "b.book.id, b.book.name, b.book.isbn, STRING_AGG(CAST(b.user.id AS string ), ',')) " +
//            "FROM Borrow b " +
//            "WHERE b.isReturned = false " +
//            "GROUP BY b.book.id, b.book.name, b.book.isbn")
//    List<AllBorrowedResponse> findAllBorrowedBooks();




//    @Query("SELECT b FROM Borrow b WHERE b.user.id = :userId and b.isReturned = false and b.book.name LIKE :keyword ORDER BY b.borrowedAt DESC")
//    Page<Borrow> findAllBorrowedByUserId(@Param("userId") String userId ,@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Borrow b WHERE b.user.id = :userId and b.isReturned = false and b.book.name LIKE :keyword ORDER BY b.borrowedAt DESC")
    Page<Borrow> findAllBorrowsByUserId(@Param("userId") UUID userId,@Param("keyword") String keyword, PageRequest pageRequest);

    @Query("SELECT b FROM Borrow b WHERE b.book.id = :bookId AND b.book.name LIKE %:keyword% and b.isReturned = false ORDER BY b.borrowedAt DESC")
    Page<Borrow> findAllBorrowsByBookId(@Param("bookId") Long bookId, @Param("keyword") String keyword, Pageable pageable);
}
