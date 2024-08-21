package com.hmall.search.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
    protected Long total;
    protected Long pages;
    protected List<T> list;

    public static <T> PageDTO<T> fullPage(Long total, Long pages, List<T> list) {
        return new PageDTO<>(total, pages, list);
    }
}
