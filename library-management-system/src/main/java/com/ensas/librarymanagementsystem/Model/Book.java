package com.ensas.librarymanagementsystem.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@Slf4j
@Table(name = "BOOK")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 6, max = 25)
    @Column(length = 55,nullable = false,unique = true)
    private String isbn;

    @NotBlank
    @Size(min = 6,max = 100)
    @Column(length = 100, nullable = false,unique = true)
    private String name;

    @NotBlank
    @Size(min = 15)
    @Column(nullable = false, length = 100000)
    private String description;
    private int quantity;
    @ManyToMany
    @JoinTable(name = "BOOK_AUTHOR",
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id", referencedColumnName = "id")} )
    private List<Author> authors = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "BOOK_CATEGORY",
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    private List<Category> categories = new ArrayList<>();

    public void addAuthor(Author author){
        this.authors.add(author);
        author.getBooks().add(this);
    }
    public void addCategory(Category category){
        this.categories.add(category);
        category.getBooks().add(this);
    }
}
