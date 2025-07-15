package edu.fbansept.m2i2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to measure method execution time
 * 
 * Usage example:
 * @MeasureTime
 * public void myMethod() { ... }
 * 
 * @MeasureTime(logLevel = "DEBUG", unit = "MILLISECONDS")
 * public void anotherMethod() { ... }
 */
@Target(ElementType.METHOD)              // Peut être utilisée uniquement sur les méthodes
@Retention(RetentionPolicy.RUNTIME)      // Disponible à l'exécution (pour la réflexion)
public @interface MeasureTime {
    
    /**
     * Log level to display execution time
     * @return the log level (default "INFO")
     */
    String logLevel() default "INFO";
    
    /**
     * Time unit for display
     * @return the time unit (default "MILLISECONDS")
     */
    String unit() default "MILLISECONDS";
    
    /**
     * Custom message to display
     * @return the message (default empty, uses method name)
     */
    String message() default "";
    
    /**
     * Include method parameters in log
     * @return true to include parameters, false otherwise
     */
    boolean includeParameters() default false;
}