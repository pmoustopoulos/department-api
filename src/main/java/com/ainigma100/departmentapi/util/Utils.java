package com.ainigma100.departmentapi.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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

}
