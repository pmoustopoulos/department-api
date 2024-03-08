package com.ainigma100.departmentapi.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


public class Utils {

    // Private constructor to prevent instantiation
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static Pageable createPageableBasedOnPageAndSizeAndSorting(List<SortItem> sortList, Integer page, Integer size) {

        List<Order> orders = new ArrayList<>();

        if (sortList != null) {
            // iterate the SortList to see based on which attributes we are going to Order By the results.
            for(SortItem sortValue : sortList) {
                orders.add(new Order(sortValue.getDirection(), sortValue.getField()));
            }
        }


        return PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(10),
                Sort.by(orders));
    }

    public static String getCurrentDateAsString() {

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return dateTimeFormatter.format(localDate);
    }

    /**
     * This method is used to retrieve a nested object or if a null pointer exception occurs
     * a default value would be set.
     * @param supplier
     * @param defaultValue
     * @return
     * @param <T>
     */
    public static <T> T retrieveValueOrSetDefault(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        }
        catch (NullPointerException ex) {
            return defaultValue;
        }
    }

}
