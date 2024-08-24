package com.ensas.librarymanagementsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupedBorrowedBooksResponse {
    private Long bookId;
    private String bookName;
    private String isbn;
    private List<BorrowerInfo> borrowers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BorrowerInfo {
        private UUID userId;
        private String username;
        private LocalDate borrowedAt;
    }
}
