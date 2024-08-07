package com.ensas.librarymanagementsystem.serviceImpl;


import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Borrow;
import com.ensas.librarymanagementsystem.Model.security.User;
import com.ensas.librarymanagementsystem.exceptions.BookNotFoundException;
import com.ensas.librarymanagementsystem.repositories.BookRepository;
import com.ensas.librarymanagementsystem.repositories.BorrowRepository;
import com.ensas.librarymanagementsystem.repositories.UserRepository;
import com.ensas.librarymanagementsystem.service.BookService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;

    @Override
    public Book saveBook(Book book) {
        return  bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException("Book with id : " + id + " not found"));
//        if(StringUtils.isNotBlank(book.getName())){
//            book.setName(book.getName());
//        }
        bookRepository.save(book);
        return book;
    }
    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByUsername(currentPrincipalName).get();
    }
    @Override
    public Page<Book> getBooks(String keyword, int page, int size) {
        keyword = keyword+"%";
        log.info(keyword);
        return bookRepository.findBookByName(keyword, (Pageable) PageRequest.of(page,size));

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
