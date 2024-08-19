package com.ensas.librarymanagementsystem.serviceImpl;


import com.ensas.librarymanagementsystem.Model.Author;
import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Borrow;
import com.ensas.librarymanagementsystem.Model.Category;
import com.ensas.librarymanagementsystem.Model.security.User;
import com.ensas.librarymanagementsystem.dto.request.BookUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.BookResponse;
import com.ensas.librarymanagementsystem.exceptions.BookNotFoundException;
import com.ensas.librarymanagementsystem.mapper.BookMapper;
import com.ensas.librarymanagementsystem.repositories.*;
import com.ensas.librarymanagementsystem.service.BookService;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    @Override
    public Book saveBook(Book book) {
        return  bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookResponse updateBook(Long id, BookUpdateRequest bookUpdateRequest) {
        // Tìm cuốn sách hiện có
        Book existingBook = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException("Book with id " + id + " not found"));

        // Cập nhật thông tin cuốn sách
        if (StringUtils.isNotBlank(bookUpdateRequest.getName())) {
            existingBook.setName(bookUpdateRequest.getName());
        }
        if (StringUtils.isNotBlank(bookUpdateRequest.getIsbn())) {
            existingBook.setIsbn(bookUpdateRequest.getIsbn());
        }
        if (StringUtils.isNotBlank(bookUpdateRequest.getDescription())) {
            existingBook.setDescription(bookUpdateRequest.getDescription());
        }
        if (bookUpdateRequest.getQuantity() >= 0) {
            existingBook.setQuantity(bookUpdateRequest.getQuantity());
        }

        // Cập nhật danh sách các tác giả
        if (bookUpdateRequest.getAuthorIds() != null) {
            List<Author> authors = authorRepository.findAllById(bookUpdateRequest.getAuthorIds());
            existingBook.setAuthors(authors);
        }

        // Cập nhật danh sách các danh mục
        if (bookUpdateRequest.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(bookUpdateRequest.getCategoryIds());
            existingBook.setCategories(categories);
        }
        Book updatedBook = bookRepository.save(existingBook);

        // Lưu cuốn sách đã cập nhật
        return bookMapper.toBookResponse(updatedBook);
    }


    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByUsername(currentPrincipalName).get();
    }
//    @Override
//    public Page<Book> getBooks(String keyword, int page, int size) {
//        keyword = keyword+"%";
//        log.info(keyword);
//        return bookRepository.findBookByName(keyword, (Pageable) PageRequest.of(page,size));
//
//    }
@Override
public Page<BookResponse> getBooks(String keyword, int page, int size) {
    // Sử dụng Pageable để thực hiện pagination
    Pageable pageable = PageRequest.of(page, size);

    // Sử dụng repository để tìm kiếm với keyword và pageable
    String keywordWithWildcard = "%" + keyword.toLowerCase() + "%";
    Page<Book> booksPage = bookRepository.findBookByName(keywordWithWildcard, pageable);

    // Map từ Page<Book> sang Page<BookResponse>
    Page<BookResponse> bookResponsesPage = booksPage.map(bookMapper::toBookResponse);

    return bookResponsesPage;
}

    @Override
    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() ->
        new BookNotFoundException("book with id "+ id +" not found"));
    }

    @Override
    public boolean borrowBook(Long id, String date) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException("Book with id " + id + " not found"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dat1 = LocalDate.parse(date, dateTimeFormatter);
        Period period = Period.ofMonths(3);
        if (dat1.isBefore(LocalDate.now().plus(period))){
            Borrow borrow = new Borrow();
            borrow.setUser(getUser());
            borrow.setBook(book);
            borrow.setReturned(false);
            borrow.setBorrowedAt(LocalDate.now());
            borrowRepository.save(borrow);
            book.setQuantity(book.getQuantity()-1);
            bookRepository.save(book);
            return true;

        }
        return false;
    }

    @Override
    public boolean checkIfAlreadyBorrowed(Long id) {
        return borrowRepository.findAlreadyBorrowed(id, getUser().getId()).isPresent();
    }

    @Override
    public boolean checkBookQuantity(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException("Book with id " + id + " not found"));
        return book.getQuantity() != 0;
    }

    @Override
    public Page<Borrow> getBorrowedBooks(String keyword, int page, int size) {
        keyword = keyword + "%";
        return borrowRepository.findBorrowedBooks(getUser().getId(),keyword, PageRequest.of(page, size));
    }

    @Override
    public void returnBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException("Book with id " + id + " not found"));
        Borrow borrow = borrowRepository.findAlreadyBorrowed(book.getId(),getUser().getId()).orElseThrow(
                ()-> new BookNotFoundException("Book with id " + id + " is not borrowed")
        );
        borrow.setReturned(true);
        borrow.setReturnedAt(LocalDate.now());
        borrowRepository.save(borrow);
    }
}
