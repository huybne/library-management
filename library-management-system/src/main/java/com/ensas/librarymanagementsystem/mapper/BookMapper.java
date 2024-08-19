package com.ensas.librarymanagementsystem.mapper;

import com.ensas.librarymanagementsystem.Model.Author;
import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Category;
import com.ensas.librarymanagementsystem.dto.request.BookRequest;
import com.ensas.librarymanagementsystem.dto.response.BookResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {
    public BookResponse toBookResponse(Book book) {
        List<Category> categories = book.getCategories() != null
                ?book.getCategories().stream().collect(Collectors.toUnmodifiableList()) : null;
        List<Author> authors = book.getAuthors() != null
                ?book.getAuthors().stream().collect(Collectors.toUnmodifiableList()) : null;
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .quantity(book.getQuantity())
                .categories(categories)
                .authors(authors)
                .build();
    }


}
