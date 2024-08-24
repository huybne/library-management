package com.ensas.librarymanagementsystem.serviceImpl;


import com.ensas.librarymanagementsystem.Model.Author;
import com.ensas.librarymanagementsystem.Model.Book;
import com.ensas.librarymanagementsystem.Model.Borrow;
import com.ensas.librarymanagementsystem.Model.Category;
import com.ensas.librarymanagementsystem.Model.security.User;
import com.ensas.librarymanagementsystem.dto.request.BookUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.BookResponse;
import com.ensas.librarymanagementsystem.dto.response.GroupedBorrowedBooksResponse;
import com.ensas.librarymanagementsystem.exceptions.BookNotFoundException;
import com.ensas.librarymanagementsystem.mapper.BookMapper;
import com.ensas.librarymanagementsystem.repositories.*;
import com.ensas.librarymanagementsystem.service.BookService;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    @Override
    public Book saveBook(Book book) {
        return  bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookResponse updateBook(Long id, BookUpdateRequest bookUpdateRequest) {
        // Tìm cuốn sách hiện có
        Book existingBook = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException("Book with id " + id + " not found"));

        // Cập nhật thông tin cuốn sách
        if (StringUtils.isNotBlank(bookUpdateRequest.getName())) {
            existingBook.setName(bookUpdateRequest.getName());
        }
        if (StringUtils.isNotBlank(bookUpdateRequest.getIsbn())) {
            existingBook.setIsbn(bookUpdateRequest.getIsbn());
        }
        if (StringUtils.isNotBlank(bookUpdateRequest.getDescription())) {
            existingBook.setDescription(bookUpdateRequest.getDescription());
        }
        if (bookUpdateRequest.getQuantity() >= 0) {
            existingBook.setQuantity(bookUpdateRequest.getQuantity());
        }

        // Cập nhật danh sách các tác giả
        if (bookUpdateRequest.getAuthorIds() != null) {
            List<Author> authors = authorRepository.findAllById(bookUpdateRequest.getAuthorIds());
            existingBook.setAuthors(authors);
        }

        // Cập nhật danh sách các danh mục
        if (bookUpdateRequest.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(bookUpdateRequest.getCategoryIds());
            existingBook.setCategories(categories);
        }
        Book updatedBook = bookRepository.save(existingBook);

        // Lưu cuốn sách đã cập nhật
        return bookMapper.toBookResponse(updatedBook);
    }


    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByUsername(currentPrincipalName).get();
    }
//    @Override
//    public Page<Book> getBooks(String keyword, int page, int size) {
//        keyword = keyword+"%";
//        log.info(keyword);
//        return bookRepository.findBookByName(keyword, (Pageable) PageRequest.of(page,size));
//
//    }
@Override
public Page<BookResponse> getBooks(String keyword, int page, int size) {
    // Sử dụng Pageable để thực hiện pagination
    Pageable pageable = PageRequest.of(page, size);

    // Sử dụng repository để tìm kiếm với keyword và pageable
    String keywordWithWildcard = "%" + keyword.toLowerCase() + "%";
    Page<Book> booksPage = bookRepository.findBookByName(keywordWithWildcard, pageable);

    // Map từ Page<Book> sang Page<BookResponse>
    Page<BookResponse> bookResponsesPage = booksPage.map(bookMapper::toBookResponse);

    return bookResponsesPage;
}

    @Override
    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() ->
        new BookNotFoundException("book with id "+ id +" not found"));
    }

    @Override
    public boolean borrowBook(Long id, String date) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException("Book with id " + id + " not found"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dat1 = LocalDate.parse(date, dateTimeFormatter);
        Period period = Period.ofMonths(3);
        if (dat1.isBefore(LocalDate.now().plus(period)) ){
            Borrow borrow = new Borrow();
            borrow.setUser(getUser());
            borrow.setBook(book);
            borrow.setReturned(false);
            borrow.setBorrowedAt(dat1);
            borrowRepository.save(borrow);
            book.setQuantity(book.getQuantity()-1);
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkIfAlreadyBorrowed(Long id) {
        return borrowRepository.findAlreadyBorrowed(id, getUser().getId()).isPresent();
    }
    @Override
    public boolean checkIfReturned(Long id) {
        return borrowRepository.findReturnedBorrowed(id, getUser().getId()).isPresent();
    }
    @Override
    public boolean checkBookQuantity(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException("Book with id " + id + " not found"));
        return book.getQuantity() != 0;
    }

    @Override
    public Page<Borrow> getBorrowedBooks(String keyword, int page, int size) {
        keyword = keyword + "%";
        return borrowRepository.findBorrowedBooks(getUser().getId(),keyword, PageRequest.of(page, size));
    }
//    @Override
//    public Page<Borrow> getAllB(String keyword, int page, int size) {
//        keyword = keyword + "%";
//        return borrowRepository.findAll();
//    }
    @Override
    public Page<GroupedBorrowedBooksResponse> getAllGroupedBorrows(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Lấy tất cả các borrow chưa trả theo từ khóa
        Page<Borrow> borrowPage = borrowRepository.findAllByKeyword(keyword, pageable);

        // Map để nhóm các Borrow theo bookId
        Map<Long, List<Borrow>> groupedByBook = borrowPage.stream()
                .collect(Collectors.groupingBy(borrow -> borrow.getBook().getId()));

        // Chuyển đổi Map thành danh sách DTO
        List<GroupedBorrowedBooksResponse> groupedResponse = groupedByBook.entrySet().stream()
                .map(entry -> {
                    Long bookId = entry.getKey();
                    List<Borrow> borrows = entry.getValue();
                    Book book = borrows.get(0).getBook(); // Giả định tất cả các borrow trong nhóm có cùng sách

                    // Tạo danh sách các BorrowerInfo
                    List<GroupedBorrowedBooksResponse.BorrowerInfo> borrowers = borrows.stream()
                            .map(borrow -> GroupedBorrowedBooksResponse.BorrowerInfo.builder()
                                    .userId(borrow.getUser().getId())
                                    .username(borrow.getUser().getUsername())
                                    .borrowedAt(borrow.getBorrowedAt())
                                    .build())
                            .collect(Collectors.toList());

                    // Tạo và trả về DTO cho nhóm hiện tại
                    return GroupedBorrowedBooksResponse.builder()
                            .bookId(bookId)
                            .bookName(book.getName())
                            .isbn(book.getIsbn())
                            .borrowers(borrowers)
                            .build();
                })
                .collect(Collectors.toList());

        // Tạo lại Page từ danh sách đã nhóm
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), groupedResponse.size());
        Page<GroupedBorrowedBooksResponse> pageResult = new PageImpl<>(groupedResponse.subList(start, end), pageable, groupedResponse.size());

        return pageResult;
    }

    //    public Page<AllBorrowedResponse> BorrowedGroupByUser( String keyword, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        String keywordWithWildcard = "%" + keyword.toLowerCase() + "%";
//        Optional<Borrow> borrow = borrowRepository.findAllGroup();
//
//    }
//@Override
//public List<AllBorrowedResponse> getAllBorrowedBooks() {
//    return borrowRepository.findAllBorrowedBooks();
//}
//    @Override
//    public Page<Borrow> getAllBorrowedByUser(String userId, String keyword, int page, int size) {
//        return borrowRepository.findAllBorrowedByUserId(userId, keyword + "%", PageRequest.of(page, size));
//    }
    @Override
    public Page<Borrow> getAllBorrowsByUserId(UUID userId, String keyword, int page, int size) {
        return borrowRepository.findAllBorrowsByUserId(userId, keyword + "%", PageRequest.of(page, size));
    }

    @Override
    public Page<Borrow> getAllBorrowsByBookId(Long bookId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return borrowRepository.findAllBorrowsByBookId(bookId, keyword, pageable);
    }

    @Override
    public void returnBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException("Book with id " + id + " not found"));
        Borrow borrow = borrowRepository.findAlreadyBorrowed(book.getId(),getUser().getId()).orElseThrow(
                ()-> new BookNotFoundException("Book with id " + id + " is not borrowed")
        );
        borrow.setReturned(true);
        borrow.setReturnedAt(LocalDate.now());
        borrowRepository.save(borrow);
        book.setQuantity(book.getQuantity()+1);
        bookRepository.save(book);
    }
}
