package com.ensas.librarymanagementsystem.service;

import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Borrow;
import com.ensas.librarymanagementsystem.dto.request.BookUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.BookResponse;
import com.ensas.librarymanagementsystem.dto.response.GroupedBorrowedBooksResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BookService {
    Book saveBook(Book book);
    void deleteBook(Long id);
    BookResponse updateBook(Long id, BookUpdateRequest bookUpdateRequest);

    Page<BookResponse> getBooks(String keyword, int page, int size);

    // Page<Book> getBooks(String keyword, int page, int size);
    Book getBook(Long id);
    boolean borrowBook(Long id, String date);

    boolean checkIfAlreadyBorrowed(Long id);

    boolean checkIfReturned(Long id);

    boolean checkBookQuantity(Long id);

    Page<Borrow> getBorrowedBooks(String keyword, int page, int size);

  //  Page<Borrow> getAllBorrowedBooks(String keyword, int page, int size);



//   Page<Borrow> getAllB(String keyword, int page, int size);

//
//    Page<Borrow> getAllBorrowedByUser(String userId, String keyword, int page, int size);
//
//  //  List<Borrow> getAllBorrowsByUserId(Long userId);


    //    public Page<AllBorrowedResponse> BorrowedGroupByUser( String keyword, int page, int size) {
    //        Pageable pageable = PageRequest.of(page, size);
    //        String keywordWithWildcard = "%" + keyword.toLowerCase() + "%";
    //        Optional<Borrow> borrow = borrowRepository.findAllGroup();
    //
    //    }
 //   List<AllBorrowedResponse> getAllBorrowedBooks();

    Page<GroupedBorrowedBooksResponse> getAllGroupedBorrows(String keyword, int page, int size);

    Page<Borrow> getAllBorrowsByUserId(UUID userId, String keyword, int page, int size);

    Page<Borrow> getAllBorrowsByBookId(Long bookId, String keyword, int page, int size);

    void returnBook(Long id);
}
