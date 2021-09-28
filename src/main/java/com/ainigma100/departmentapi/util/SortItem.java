package com.ainigma100.departmentapi.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Sort;

/*
 * This class is used to specify the field we want to sort by
 * and the direction (ASC or DESC) we want to use on that field.
 *
 * It is used as part of the Pageable. See the example on method searchOpportunityRecords
 * which exists in the OpportunityController
 *
 * For more details here: https://www.baeldung.com/spring-data-jpa-pagination-sorting
 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SortItem {

    private String field;
    private Sort.Direction direction;

}
