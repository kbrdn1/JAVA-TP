package edu.fbansept.m2i2;

import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionInterceptor {

  //Intercepts all model validation errors (@NotNull, @NotBlank, @Size ...)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody // Ensures that the response is sent in JSON format (in the response body)
  public Map<String, Object> handleMethodArgumentNotValidException(
    MethodArgumentNotValidException ex
  ) {
    Map<String, Object> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getAllErrors()
      .forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      });
    return errors;
  }

  //Intercepts all database constraint errors
  // (foreign key constraint, unique fields ...)
  // Note: also catches nullable = false constraints
  // if @NotNull annotation is missing in the model for example
  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT) // Code de retour
  @ResponseBody // Ensures that the response is sent in JSON format (in the response body)
  public Map<String, Object> handleDataIntegrityViolationException(
    DataIntegrityViolationException ex
  ) {
    return Map.of("message", "A data integrity constraint violation was detected");
  }
}
