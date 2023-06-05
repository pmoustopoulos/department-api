package com.ainigma100.departmentapi.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Utils {


    public static Pageable createPageableBasedOnPageAndSizeAndSorting(List<SortItem> sortList, Integer page, Integer size) {

        List<Order> orders = new ArrayList<>();

        if(sortList != null) {
            // iterate the SortList to see based on which attributes we are going to Order By the results.
            for(SortItem sortValue : sortList) {
                orders.add(new Order(sortValue.getDirection(), sortValue.getField()));
            }
        }

        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 0;
        }

        return PageRequest.of(page, size, Sort.by(orders));
    }

    public static String getCurrentDateAsString() {

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return dateTimeFormatter.format(localDate);
    }

}
