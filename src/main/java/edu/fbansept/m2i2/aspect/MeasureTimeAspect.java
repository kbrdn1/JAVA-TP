package edu.fbansept.m2i2.aspect;

import edu.fbansept.m2i2.annotation.MeasureTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Aspect to measure execution time of methods annotated with @MeasureTime
 */
@Aspect
@Component
public class MeasureTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(MeasureTimeAspect.class);

    /**
     * Intercepts all methods annotated with @MeasureTime
     * and measures their execution time
     */
    @Around("@annotation(measureTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, MeasureTime measureTime) throws Throwable {
        
        // Get method and class name
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        // Build custom message or default
        String message = measureTime.message().isEmpty() 
            ? String.format("%s.%s", className, methodName)
            : measureTime.message();
        
        // Add parameters if requested
        if (measureTime.includeParameters()) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {
                message += " with parameters: " + Arrays.toString(args);
            }
        }
        
        // Start measurement
        long startTime = System.nanoTime();
        
        try {
            // Execute original method
            Object result = joinPoint.proceed();
            
            // End measurement and calculate time
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;
            
            // Convert according to requested unit
            double convertedTime = convertTime(executionTime, measureTime.unit());
            String unit = measureTime.unit().toLowerCase();
            
            // Log according to requested level
            String logMessage = String.format("⏱️  %s executed in %.2f %s", message, convertedTime, unit);
            logAtLevel(measureTime.logLevel(), logMessage);
            
            return result;
            
        } catch (Exception e) {
            // In case of error, still log execution time
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;
            double convertedTime = convertTime(executionTime, measureTime.unit());
            String unit = measureTime.unit().toLowerCase();
            
            String errorMessage = String.format("❌ %s failed after %.2f %s - Error: %s", 
                message, convertedTime, unit, e.getMessage());
            logAtLevel("ERROR", errorMessage);
            
            throw e; // Re-throw exception
        }
    }
    
    /**
     * Converts time from nanoseconds to requested unit
     */
    private double convertTime(long nanoTime, String unit) {
        return switch (unit.toUpperCase()) {
            case "NANOSECONDS" -> nanoTime;
            case "MICROSECONDS" -> nanoTime / 1_000.0;
            case "MILLISECONDS" -> nanoTime / 1_000_000.0;
            case "SECONDS" -> nanoTime / 1_000_000_000.0;
            default -> nanoTime / 1_000_000.0; // Default to milliseconds
        };
    }
    
    /**
     * Log message according to specified level
     */
    private void logAtLevel(String level, String message) {
        switch (level.toUpperCase()) {
            case "TRACE" -> logger.trace(message);
            case "DEBUG" -> logger.debug(message);
            case "INFO" -> logger.info(message);
            case "WARN" -> logger.warn(message);
            case "ERROR" -> logger.error(message);
            default -> logger.info(message); // Default to INFO
        }
    }
}