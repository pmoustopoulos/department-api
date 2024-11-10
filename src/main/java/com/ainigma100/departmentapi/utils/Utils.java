package com.ainigma100.departmentapi.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
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


    public static void setEncodingForLocale(ReloadableResourceBundleMessageSource messageSource, Locale locale) {
        String encoding = determineEncoding(locale);
        messageSource.setDefaultEncoding(encoding);
    }

    private static String determineEncoding(Locale locale) {

        String language = locale.getLanguage().toLowerCase();

        return switch (language) {
            case "es", "de" -> "ISO-8859-1";
            default -> "UTF-8";
        };
    }


    /**
     * Retrieves a value from a Supplier or sets a default value if a NullPointerException occurs.
     * Usage example:
     *
     * <pre>{@code
     * // Example 1: Retrieve a list or provide an empty list if null
     * List<Employee> employeeList = Utils.retrieveValueOrSetDefault(() -> someSupplierMethod(), new ArrayList<>());
     *
     * // Example 2: Retrieve an Employee object or provide a default object if null
     * Employee emp = Utils.retrieveValueOrSetDefault(() -> anotherSupplierMethod(), new Employee());
     * }</pre>
     *
     * @param supplier     the Supplier providing the value to retrieve
     * @param defaultValue the default value to return if a NullPointerException occurs
     * @return the retrieved value or the default value if a NullPointerException occurs
     * @param <T>          the type of the value
     */
    public static <T> T retrieveValueOrSetDefault(Supplier<T> supplier, T defaultValue) {

        try {
            return supplier.get();

        } catch (NullPointerException ex) {

            log.error("Error while retrieveValueOrSetDefault {}", ex.getMessage());

            return defaultValue;
        }
    }

    public static String getClientIP(HttpServletRequest request) {

        String clientIP = request.getHeader("Client-IP");

        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("X-Forwarded-For");
        }

        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("X-Real-IP");
        }

        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getRemoteAddr();
        }

        return clientIP != null ? clientIP : "Unknown";
    }

}
