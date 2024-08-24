package com.ensas.librarymanagementsystem.rest;

import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Author;
import com.ensas.librarymanagementsystem.Model.Category;
import com.ensas.librarymanagementsystem.service.AuthorService;
import com.ensas.librarymanagementsystem.util.GeneratePagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
@Slf4j
public class AuthorController {
    private final AuthorService authorService;
    private final GeneratePagination generatePagination;

    @GetMapping
    public ResponseEntity<?> getAuthors(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "5") int size) {
        log.info("Requested authors page: {}", page);
        Page<Author> authorPage = authorService.getAuthors(keyword, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("authors", authorPage.getContent());
        response.put("pagination", generatePagination.pagination(page));
        response.put("totalPages", authorPage.getTotalPages());
        response.put("totalElements", authorPage.getTotalElements());
        response.put("keyword", keyword);
        response.put("currentSize", size);
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable("id") Long id) {
        Author author = authorService.getAuthor(id);
        return ResponseEntity.ok(author);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/books")

    public ResponseEntity<?> getAuthorBooks(@PathVariable("id") Long id,
                                            @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "5") int size) {
        Page<Book> categoryBooks = authorService.getAuthorBooks(id, keyword, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("books", categoryBooks.getContent());
        response.put("pagination", generatePagination.pagination(page));
        response.put("totalPages", categoryBooks.getTotalPages());
        response.put("totalElements", categoryBooks.getTotalElements());
        response.put("keyword", keyword);
        response.put("currentSize", size);
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveAuthor(@Valid @RequestBody Author author, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Author savedAuthor = authorService.addAuthor(author);
        return ResponseEntity.ok(savedAuthor);
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAuthor(@PathVariable("id") Long id,
                                          @Valid @RequestBody Author author,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Author updatedAuthor = authorService.updateAuthor(id, author);
        return ResponseEntity.ok(updatedAuthor);
    }
}
