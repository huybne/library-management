package com.ensas.librarymanagementsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {
    String name;
    String isbn;
    String description;
    int quantity;
    List<Long> authorIds;
    List<Long> categoryIds;
}
