package org.example.todo.exception;

import jakarta.validation.UnexpectedTypeException;
import lombok.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Classe interna per la risposta di errore
    public static class ErrorResponse {
        @Getter  @Setter
        private String errorMessage;

        @Getter @Setter
        private String errorCode;

        public ErrorResponse(String errorCode,String errorMessage){
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }

    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Utente già registrato",e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Utente non trovato",e.getMessage()));
    }

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTodoNotFoundException(TodoNotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Todo non trovato",e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Unauthorized",e.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException e){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid Credentials",e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        Map<String,String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach((error)->
                        errors.put(error.getField(),error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Type mismatch",
                "Valore non valido. Controlla i campi enum: TodoStatus (TODO, IN_PROGRESS, DONE), Priority (LOW, MEDIUM, HIGH)"));
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("INVALID_VALUE",
                        "Valore non valido. Controlla i campi enum: TodoStatus (TODO, IN_PROGRESS, DONE), Priority (LOW, MEDIUM, HIGH)"));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("404","Risosrsa non trovata"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal Server Error","Errore interno al server"));
    }


}
