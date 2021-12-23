package com.jungdam.common.utils;

import java.util.Objects;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtils {

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;

    private PageUtils() {
    }

    public static Pageable of(Integer size) {
        if (Objects.isNull(size)) {
            return PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        }
        return PageRequest.of(DEFAULT_PAGE, size);
    }

    public static Pageable of(Integer size, Sort sort) {
        if (Objects.isNull(size)) {
            return PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, sort);
        }
        return PageRequest.of(DEFAULT_PAGE, size, sort);
    }
}