package com.danube.danube.controller.advice;

import com.danube.danube.model.error.UserErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class Advice {
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public final ResponseEntity<?> handleException(Exception e){
        if(e instanceof SQLIntegrityConstraintViolationException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                            handleSQLIntegrityConstrainViolationException()
                    );
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(handleInternalServerError());
        }
    }

    public UserErrorMessage handleSQLIntegrityConstrainViolationException(){
        return new UserErrorMessage(String.format("Already existing email!"));
    }

    public UserErrorMessage handleInternalServerError(){
        return new UserErrorMessage("Unfortunately something went wrong on the server!");
    }
}
