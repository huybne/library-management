package com.ensas.librarymanagementsystem.service;

import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Borrow;
import com.ensas.librarymanagementsystem.dto.request.BookUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.BookResponse;
import org.springframework.data.domain.Page;

public interface BookService {
    Book saveBook(Book book);
    void deleteBook(Long id);
    BookResponse updateBook(Long id, BookUpdateRequest bookUpdateRequest);

    Page<BookResponse> getBooks(String keyword, int page, int size);

    // Page<Book> getBooks(String keyword, int page, int size);
    Book getBook(Long id);
    boolean borrowBook(Long id, String date);

    boolean checkIfAlreadyBorrowed(Long id);

    boolean checkBookQuantity(Long id);

    Page<Borrow> getBorrowedBooks(String keyword, int page, int size);

    void returnBook(Long id);
}
