package com.ensas.librarymanagementsystem.util;

import org.springframework.stereotype.Component;

@Component
public class GeneratePagination {

    public int[] pagination(Integer requestedPage) {
        if (requestedPage == null) {
            return new int[]{0};
        }

        int[] pagination;
        if (requestedPage == 0 || requestedPage == 1 || requestedPage == 2) {
            pagination = new int[]{0, 1, 2};
        } else if (requestedPage % 3 == 0) {
            pagination = new int[]{requestedPage, requestedPage + 1, requestedPage + 2};
        } else if (requestedPage % 3 == 1) {
            pagination = new int[]{requestedPage - 1, requestedPage, requestedPage + 1};
        } else if (requestedPage % 3 == 2) {
            pagination = new int[]{requestedPage - 2, requestedPage - 1, requestedPage};
        } else {
            pagination = new int[]{requestedPage};
        }
        return pagination;
    }
}