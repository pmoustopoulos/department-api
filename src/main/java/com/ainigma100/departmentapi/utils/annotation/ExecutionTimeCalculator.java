package com.ainigma100.departmentapi.utils.annotation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExecutionTimeCalculator {

//	\033[38;2;<r>;<g>;<b>m     #Select RGB foreground color
//	\033[48;2;<r>;<g>;<b>m     #Select RGB background color

	// more colors can be found here: 
	// https://www.lihaoyi.com/post/BuildyourownCommandLinewithANSIescapecodes.html#256-colors
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\033[38;2;0;189;25m";
	public static final String ANSI_YELLOW = "\033[38;2;255;255;0m";
	public static final String ANSI_BLUE = "\u001B[38;5;33m";
	public static final String ANSI_BLACK = "\u001B[38;5;234m";
	public static final String ANSI_GREY = "\u001B[38;5;249m";
	public static final String ANSI_RED = "\u001B[38;5;196m";
	public static final String ANSI_ORANGE = "\u001B[38;5;208m";


	// I have to put the whole path to my custom annotation class as a parameter of @annotation
	// @Around can perform custom behavior Before and After the method Invocation
	@Around("@annotation(com.ainigma100.departmentapi.utils.annotation.ExecutionTime)")
	public Object getExecutionTime(ProceedingJoinPoint proJoinPoint) throws Throwable {

		// get time when method execution starts
		long startTimeInMillis = System.currentTimeMillis();
		
		LocalDateTime startLocalDateTime = Instant.ofEpochMilli(startTimeInMillis)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		// execute method
		Object obj = proJoinPoint.proceed();
		
		
		// get time when method execution ends
		long endTimeInMillis = System.currentTimeMillis();
		
		LocalDateTime endLocalDateTime = Instant.ofEpochMilli(endTimeInMillis)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		// get elapsed time in milliseconds
		long executionTimeInMilliseconds = (endTimeInMillis - startTimeInMillis);
		
		// milliseconds to minutes. 
        long minutes = (executionTimeInMilliseconds / 1000) / 60;
        
        // milliseconds to seconds 
        long seconds = (executionTimeInMilliseconds / 1000) % 60;


		log.info("Method " + ANSI_YELLOW + proJoinPoint.getSignature().getName() + ANSI_RESET
				+ " called at: " + ANSI_ORANGE + startLocalDateTime + ANSI_RESET
				+ " and finished execution at: " + ANSI_ORANGE + endLocalDateTime + ANSI_RESET
				+ ". The execution time was: " + ANSI_GREEN + minutes + " minutes" + ANSI_RESET
				+ " and " + ANSI_GREEN + seconds + " seconds." + ANSI_RESET);
		
		return obj;
	}
}
