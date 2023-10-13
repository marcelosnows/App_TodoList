package br.com.marcelosnows.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // usado para criar classes globais para tratamento exceções de erros. Havendo exceção, passa por esta Annotation. 
public class ExceptionHandlerController {
  
  @ExceptionHandler(HttpMessageNotReadableException.class) // Informando o método exato para este tipo de erro. Parâmetro: Tipo de exceção.
  public ResponseEntity<String> handleHttpMessageNoReadableException(HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
  };

};
