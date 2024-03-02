package com.danube.danube.controller.advice;

import com.danube.danube.custom_exception.InputTooShortException;
import com.danube.danube.custom_exception.InvalidEmailFormatException;
import com.danube.danube.custom_exception.RegistrationFieldNullException;
import com.danube.danube.model.error.UserErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class Advice {
    @ExceptionHandler({DataIntegrityViolationException.class})
    public final ResponseEntity<?> handleException(Exception e){
        if(e instanceof DataIntegrityViolationException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                            handleSQLIntegrityConstrainViolationException()
                    );
        } else if(e instanceof RegistrationFieldNullException){
            return ResponseEntity.badRequest().body(handleFieldIsNullError(e));
        } else if(e instanceof InputTooShortException){
            return ResponseEntity.badRequest().body(handleInputTooShortError(e));
        } else if(e instanceof InvalidEmailFormatException){
            return ResponseEntity.badRequest().body(handleInvalidEmailFormat());
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(handleInternalServerError());
        }
    }

    private UserErrorMessage handleSQLIntegrityConstrainViolationException(){
        return new UserErrorMessage(String.format("Already existing email!"));
    }

    private UserErrorMessage handleInternalServerError(){
        return new UserErrorMessage("Unfortunately something went wrong on the server!");
    }

    private UserErrorMessage handleFieldIsNullError(Exception e){
        return new UserErrorMessage(String.format("%s field is empty! Please enter correct value!", e.getMessage()));
    }

    private UserErrorMessage handleInputTooShortError(Exception e){
        return new UserErrorMessage(String.format("Input at %s field is too short! Input should be at least 2 character long!", e.getMessage()));
    }

    private UserErrorMessage handleInvalidEmailFormat(){
        return new UserErrorMessage("Email is invalid! Please enter a valid email address!");
    }
}
