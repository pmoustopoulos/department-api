package com.ainigma100.departmentapi.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@Getter
public class SortItem implements Serializable {


    @Schema(example = "id") // set a default sorting property for swagger
    private String field;
    private Sort.Direction direction;

}
