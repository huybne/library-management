package com.ensas.librarymanagementsystem.dto.request;

import com.ensas.librarymanagementsystem.Model.Author;
import com.ensas.librarymanagementsystem.Model.Category;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class BookUpdateRequest {
    String name;
    String isbn;
    String description;
    int quantity;
    List<Long> authorIds;
    List<Long> categoryIds;
}
