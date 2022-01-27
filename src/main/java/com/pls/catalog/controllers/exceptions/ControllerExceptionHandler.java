package com.pls.catalog.controllers.exceptions;

import com.pls.catalog.services.exceptions.ControllerNotFoundException;
import com.pls.catalog.services.exceptions.DataBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ControllerNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ControllerNotFoundException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError("Resource not found");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI()); //Muda o pacote pra pegar o caminho da requisição

        return ResponseEntity.status(status).body(error); //Retorna o status com o corpo da resposta
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> database(DataBaseException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError("Database exception");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI()); //Muda o pacote pra pegar o caminho da requisição

        return ResponseEntity.status(status).body(error); //Retorna o status com o corpo da resposta
    }
}
