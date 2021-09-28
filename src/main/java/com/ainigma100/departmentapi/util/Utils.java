package com.ainigma100.departmentapi.util;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Utils {
	
	
	public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		
		ModelMapper modelMapper = new ModelMapper();
		
	    return source
	      .stream()
	      .map(element -> modelMapper.map(element, targetClass))
	      .collect(Collectors.toList());
	}
	
	
	public static <T> T map(Object source, Class<T> targetClass) {
		
		ModelMapper modelMapper = new ModelMapper();
		
		return modelMapper.map(source, targetClass);
	}


	public static <S, T> Page<T> mapPage(Page<S> source, Class<T> targetClass) {

		ModelMapper modelMapper = new ModelMapper();

		List<S> pageAsList = source.getContent();

		List<T> mappedList = pageAsList
				.stream()
				.map(element -> modelMapper.map(element, targetClass))
				.collect(Collectors.toList());

		return new PageImpl<>(mappedList, PageRequest.of(source.getNumber(), source.getSize(), source.getSort()),
				source.getTotalElements());
	}


	/**
	 * This method is used to create a pagination based on specific attributes.
	 *
	 * @param sortList must not be {@literal null}. It represents the list of attributes that will be used to 'Order By' the results
	 * @param page is of type Integer which represents the page number
	 * @param size is of type Integer which represents the size of the results
	 * @return Pageable
	 */
	public static Pageable createPageableBasedOnPageAndSizeAndSorting(List<SortItem> sortList, Integer page, Integer size) {

		List<Order> orders = new ArrayList<>();

		if(sortList != null) {
			// iterate the SortList to see based on which attributes we are going to Order By the results.
			for(SortItem sortValue : sortList) {
				orders.add(new Order(sortValue.getDirection(), sortValue.getField()));
			}

		}

		return PageRequest.of(page, size, Sort.by(orders));

	} // end of createPageableBasedOnPageAndSizeAndSorting


}
