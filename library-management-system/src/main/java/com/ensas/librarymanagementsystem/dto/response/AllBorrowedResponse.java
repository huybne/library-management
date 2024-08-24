package com.ensas.librarymanagementsystem.dto.response;

import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.security.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllBorrowedResponse {
    private Long bookId;             // ID của sách
    private String bookName;         // Tên sách
    private String isbn;             // ISBN của sách
    private List<UUID> userIds;      // Danh sách UserId đang mượn sách
}

