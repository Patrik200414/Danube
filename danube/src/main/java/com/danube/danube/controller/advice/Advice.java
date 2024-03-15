package com.danube.danube.controller.advice;

import com.danube.danube.custom_exception.*;
import com.danube.danube.model.error.UserErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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
        } else  if(e instanceof InternalAuthenticationServiceException){
            return ResponseEntity.badRequest().body(handleEmailNotFoundException(e));
        } else if(e instanceof RegistrationFieldNullException){
            return ResponseEntity.badRequest().body(handleFieldIsNullError(e));
        } else if(e instanceof InputTooShortException){
            return ResponseEntity.badRequest().body(handleInputTooShortError(e));
        } else if(e instanceof InvalidEmailFormatException){
            return ResponseEntity.badRequest().body(handleInvalidEmailFormat());
        } else if(e instanceof BadCredentialsException){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(handleBadCredentialException());
        } else if(e instanceof NonExistingUserException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(handleNonExistingUserException());
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(handleInternalServerError(e));
        }
    }

    private UserErrorMessage handleSQLIntegrityConstrainViolationException(){
        return new UserErrorMessage(String.format("Already existing email!"));
    }

    private UserErrorMessage handleInternalServerError(Exception e){
        return new UserErrorMessage(e.getMessage());
        //return new UserErrorMessage("Unfortunately something went wrong on the server!");
    }

    private UserErrorMessage handleFieldIsNullError(Exception e){
        return new UserErrorMessage(String.format("%s field is empty! Please enter correct value!", e.getMessage()));
    }

    private UserErrorMessage handleInputTooShortError(Exception e){
        String fieldName = e.getMessage().split("-")[0];
        String minLength = e.getMessage().split("-")[1];
        return new UserErrorMessage(String.format("Input at %s field is too short! Input should be at least %s character long!", fieldName, minLength));
    }

    private UserErrorMessage handleInvalidEmailFormat(){
        return new UserErrorMessage("Email is invalid! Please enter a valid email address!");
    }

    private UserErrorMessage handleBadCredentialException(){
        return new UserErrorMessage("Invalid password! This account has different password!");
    }

    private UserErrorMessage handleEmailNotFoundException(Exception e){
        return new UserErrorMessage(e.getMessage());
    }

    private UserErrorMessage handleNonExistingUserException(){
        return new UserErrorMessage("This user doesn't exists!");
    }
}
