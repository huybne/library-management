package com.ensas.librarymanagementsystem.rest;

import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Borrow;
import com.ensas.librarymanagementsystem.dto.request.BookUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.BookResponse;
import com.ensas.librarymanagementsystem.dto.response.GroupedBorrowedBooksResponse;
import com.ensas.librarymanagementsystem.service.AuthorService;
import com.ensas.librarymanagementsystem.service.BookService;
import com.ensas.librarymanagementsystem.service.CategoryService;
import com.ensas.librarymanagementsystem.util.GeneratePagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final GeneratePagination generatePagination;

    @GetMapping
    public ResponseEntity<?> getBooks(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                      @RequestParam(name ="page", defaultValue = "0") int page,
                                      @RequestParam(name ="size", defaultValue = "5") int size) {
        Page<BookResponse> bookPage = bookService.getBooks(keyword, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("books", bookPage.getContent());
        response.put("pagination", generatePagination.pagination(page));
        response.put("totalPages", bookPage.getTotalPages());
        response.put("totalElements", bookPage.getTotalElements());
        response.put("keyword", keyword);
        response.put("currentSize", size);
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable("id") Long id) {
        Book book = bookService.getBook(id);
        return ResponseEntity.ok(book);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id,
                                        @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                        @RequestParam(name ="page", defaultValue = "0") int page,
                                        @RequestParam(name ="size", defaultValue = "5") int size) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/borrowed-books")
 //   @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBorrowed(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                         @RequestParam(name ="page", defaultValue = "0") int page,
                                         @RequestParam(name ="size", defaultValue = "10") int size) {
        Page<Borrow> borrowedBooks = bookService.getBorrowedBooks(keyword, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("borrows", borrowedBooks.getContent());
        response.put("pagination", generatePagination.pagination(page));
        response.put("totalPages", borrowedBooks.getTotalPages());
        response.put("totalElements", borrowedBooks.getTotalElements());
        response.put("keyword", keyword);
        response.put("currentSize", size);
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/grouped")
    public ResponseEntity<Page<GroupedBorrowedBooksResponse>> getAllGroupedBorrows(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<GroupedBorrowedBooksResponse> groupedBorrows = bookService.getAllGroupedBorrows(keyword,page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("borrows", groupedBorrows.getContent());
        response.put("pagination", generatePagination.pagination(page));
        response.put("totalPages", groupedBorrows.getTotalPages());
        response.put("totalElements", groupedBorrows.getTotalElements());
        response.put("keyword", keyword);
        response.put("currentSize", size);
        response.put("currentPage", page);
        return ResponseEntity.ok(groupedBorrows);
    }

//    @GetMapping("/all-borrowed-books")
//    public ResponseEntity<Page<Borrow>> getAllBorrowedBooks(@RequestParam(name = "keyword", defaultValue = "") String keyword,
//                                                            @RequestParam (name = "page", defaultValue = "0") int page,
//                                                            @RequestParam(name = "size", defaultValue = "10") int size) {
//        Page<Borrow> borrowedBooks = bookService.getAllB(keyword, page, size);
//        Map<String, Object> response = new HashMap<>();
//        response.put("borrows", borrowedBooks.getContent());
//        response.put("pagination", generatePagination.pagination(page));
//        response.put("totalPages", borrowedBooks.getTotalPages());
//        response.put("totalElements", borrowedBooks.getTotalElements());
//        response.put("keyword", keyword);
//        response.put("currentSize", size);
//        response.put("currentPage", page);
//        return ResponseEntity.ok(borrowedBooks);
//    }
    @GetMapping("/user/{userId}/borrows")
    public ResponseEntity<?> getBorrowsByUser(@PathVariable UUID userId,
                                              @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                              @RequestParam (name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Borrow> borrows = bookService.getAllBorrowsByUserId(userId, keyword, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("borrows", borrows.getContent());
        response.put("pagination", generatePagination.pagination(page));
        response.put("totalPages", borrows.getTotalPages());
        response.put("totalElements", borrows.getTotalElements());
        response.put("keyword", keyword);
        response.put("currentSize", size);
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/{bookId}/borrows")
    public ResponseEntity<?> getBorrowsByBook(@PathVariable Long bookId,
                                              @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                              @RequestParam (name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Borrow> borrows = bookService.getAllBorrowsByBookId(bookId, keyword, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("borrows", borrows.getContent());
        response.put("pagination", generatePagination.pagination(page));
        response.put("totalPages", borrows.getTotalPages());
        response.put("totalElements", borrows.getTotalElements());
        response.put("keyword", keyword);
        response.put("currentSize", size);
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }

    //    @GetMapping("/borrowed/{username}")
//    public ResponseEntity<?> getAllBorrowedBooksByUsername(@PathVariable("username") String username,
//                                                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
//                                                           @RequestParam(name = "page", defaultValue = "0") int page,
//                                                           @RequestParam(name = "size", defaultValue = "10") int size) {
//        Page<Borrow> borrowedBooks = bookService.getAllBorrowedByUser(username, page, size);
//        Map<String, Object> response = new HashMap<>();
//        response.put("borrows", borrowedBooks.getContent());
//        response.put("pagination", generatePagination.pagination(page));
//        response.put("totalPages", borrowedBooks.getTotalPages());
//        response.put("totalElements", borrowedBooks.getTotalElements());
//        response.put("keyword", keyword);
//        response.put("currentSize", size);
//        response.put("currentPage", page);
//        return ResponseEntity.ok(response);
//    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> saveBook(@Valid @RequestBody Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Book savedBook = bookService.saveBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add-book")
    public ResponseEntity<?> addBook() {
        Map<String, Object> response = new HashMap<>();
        response.put("book", new Book());
        response.put("categories", categoryService.getAllCategories());
        response.put("authors", authorService.getAllAuthors());
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookUpdateRequest book,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        BookResponse updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/return-book/{id}")
    public ResponseEntity<?> returnBook(@PathVariable("id") Long id) {
        bookService.returnBook(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/borrow/{id}")
    public ResponseEntity<?> borrow(@PathVariable("id") Long id,
                                    @RequestParam(name = "date", defaultValue = "") String date) {
        String error = "";

        // Kiểm tra nếu sách đã được mượn nhưng chưa được trả lại
        if (!bookService.checkIfAlreadyBorrowed(id)) {
            // Nếu sách chưa được mượn hoặc đã trả, thì cho phép mượn sách
            if (bookService.checkBookQuantity(id)) {
                if (bookService.borrowBook(id, date)) {
                    return ResponseEntity.noContent().build();
                } else {
                    error = "Max Period is three months from now!";
                }
            } else {
                error = "There are no books left.";
            }
        } else {
            error = "You have already borrowed this book and have not returned it.";
        }

        return ResponseEntity.badRequest().body(error);
    }



    @GetMapping("/borrow-book/{id}")
    public ResponseEntity<?> borrowBook(@PathVariable("id") Long id) {
        Book book = bookService.getBook(id);
        Map<String, Object> response = new HashMap<>();
        response.put("book", book);
        response.put("error", "");
        return ResponseEntity.ok(response);
    }
}
