package com.ensas.librarymanagementsystem.rest;

import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Category;
import com.ensas.librarymanagementsystem.service.CategoryService;
import com.ensas.librarymanagementsystem.util.GeneratePagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final GeneratePagination generatePagination;

    @GetMapping
    public ResponseEntity<?> getCategories(@RequestParam(name = "keyword",defaultValue = "") String keyword,
                                           @RequestParam(name = "page",defaultValue = "0") int page,
                                           @RequestParam(name = "size",defaultValue = "5") int size) {
        Page<Category> categoryPage = categoryService.getCategories(keyword, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("categories", categoryPage.getContent());
        response.put("pagination", generatePagination.pagination(page));
        response.put("totalPages", categoryPage.getTotalPages());
        response.put("totalElements", categoryPage.getTotalElements());
        response.put("keyword", keyword);
        response.put("currentSize", size);
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id,
                                            @RequestParam(name = "keyword",defaultValue = "") String keyword,
                                            @RequestParam(name = "page",defaultValue = "0") int page,
                                            @RequestParam(name = "size",defaultValue = "5") int size) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<?> getCategoryBooks(@PathVariable("id") Long id,
                                              @RequestParam(name = "keyword",defaultValue = "") String keyword,
                                              @RequestParam(name = "page",defaultValue = "0") int page,
                                              @RequestParam(name = "size",defaultValue = "5") int size) {
        Page<Book> categoryBooks = categoryService.getCategoryBooks(id, keyword, page, size);
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

    @GetMapping("/update-category/{id}")
    public ResponseEntity<?> getCategoryDetails(@PathVariable("id") Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<?> saveCategory(@Valid @RequestBody Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Category savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping("/add-category")
    public ResponseEntity<?> addCategory() {
        return ResponseEntity.ok(new Category());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,
                                            @Valid @RequestBody Category category,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }
}
