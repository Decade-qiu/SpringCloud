package com.hmall.search.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchVO {

    private List<String> category;

    private List<String> brand;

}
