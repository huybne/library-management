package com.ensas.librarymanagementsystem.dto.response;

import com.ensas.librarymanagementsystem.Model.Author;
import com.ensas.librarymanagementsystem.Model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String isbn;
    private String name;
    private String description;
    private int quantity;
    private List<Author> authors;
    private List<Category> categories;


}
