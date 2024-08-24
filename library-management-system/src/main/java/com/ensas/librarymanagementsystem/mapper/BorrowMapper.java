package com.ensas.librarymanagementsystem.mapper;

import com.ensas.librarymanagementsystem.Model.Borrow;
import com.ensas.librarymanagementsystem.dto.response.AllBorrowedResponse;
import com.ensas.librarymanagementsystem.dto.response.GroupedBorrowedBooksResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowMapper {
    GroupedBorrowedBooksResponse toGroup(Borrow request);
}
