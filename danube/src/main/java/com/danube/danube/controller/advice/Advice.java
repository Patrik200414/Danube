package com.danube.danube.controller.advice;

import com.danube.danube.custom_exception.input.InvalidInputException;
import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.order.OrderFailedException;
import com.danube.danube.custom_exception.product.*;
import com.danube.danube.custom_exception.user.*;
import com.danube.danube.model.error.UserErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;
import org.everit.json.schema.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;


@ControllerAdvice
public class Advice {
    private final Logger logger = LoggerFactory.getLogger(Advice.class);



    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<UserErrorMessage> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserErrorMessage("Authentication failed!")
        );
    }


    @ExceptionHandler({BadCredentialsException.class, InvalidUserCredentialsException.class})
    public ResponseEntity<UserErrorMessage> handleBadCredentialsException(Exception e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new UserErrorMessage("Invalid password! This account has different password!")
        );
    }

    @ExceptionHandler(NonExistingUserException.class)
    public ResponseEntity<UserErrorMessage> handleNonExistingUserException(NonExistingUserException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserErrorMessage("This user doesn't exists!")
        );
    }

    @ExceptionHandler(IncorrectProductObjectFormException.class)
    public ResponseEntity<UserErrorMessage> handleIncorrectProductObjectFormException(IncorrectProductObjectFormException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("Incorrect data for the selected subcategory! Please correct the information!")
        );
    }

    @ExceptionHandler(UserEntityPasswordMissMatchException.class)
    public ResponseEntity<UserErrorMessage> handleUserEntityPasswordMissMatchException(UserEntityPasswordMissMatchException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("Incorrect password for the logged in user!")
        );
    }

    @ExceptionHandler(NonExistingSubcategoryException.class)
    public ResponseEntity<UserErrorMessage> handleNonExistingSubcategoryException(NonExistingSubcategoryException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.badRequest().body(
                new UserErrorMessage("Non existing subcategory!")
        );
    }

    @ExceptionHandler(NotMatchingUserAndUpdateUserIdException.class)
    public ResponseEntity<UserErrorMessage> handleNotMatchingUserAndUpdateUserIdException(NotMatchingUserAndUpdateUserIdException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new UserErrorMessage("You can't modify other user's information!")
        );
    }

    @ExceptionHandler(NotMatchingCurrentPasswordException.class)
    public ResponseEntity<UserErrorMessage> handleNotMatchingCurrentPasswordException(NotMatchingCurrentPasswordException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("The given current password does not matches!")
        );
    }

    @ExceptionHandler(NotMatchingNewPasswordAndReenterPasswordException.class)
    public ResponseEntity<UserErrorMessage> handleNotMatchingNewPasswordAndReenterPasswordException(NotMatchingNewPasswordAndReenterPasswordException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("The reentered password does not matches the new password!")
        );
    }


    @ExceptionHandler(NonExistingProductException.class)
    public ResponseEntity<UserErrorMessage> handleNonExistingProductException(NonExistingProductException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserErrorMessage("The selected item is not found!")
        );
    }

    @ExceptionHandler(UserNotSellerException.class)
    public ResponseEntity<UserErrorMessage> handleUserNotSellerException(UserNotSellerException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new UserErrorMessage("The current user don't have seller permission!")
        );
    }

    @ExceptionHandler(MissingImageException.class)
    public ResponseEntity<UserErrorMessage> handleMissingImageException(MissingImageException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new UserErrorMessage("Missing image parameter! At least one image required!")
        );
    }

    @ExceptionHandler(NonExistingValueException.class)
    public ResponseEntity<UserErrorMessage> handleNonExistingValueException(NonExistingValueException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserErrorMessage("Unable to detect the mentioned value of the product!")
        );
    }

    @ExceptionHandler(IncorrectSellerException.class)
    public ResponseEntity<UserErrorMessage> handleIncorrectSellerException(IncorrectSellerException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("You are not the seller of the updatable product! You can only update your products!")
        );
    }

    @ExceptionHandler(ExpiredVerificationTokenException.class)
    public ResponseEntity<?> handleExpiredVerificationTokenException(ExpiredVerificationTokenException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @ExceptionHandler(OrderFailedException.class)
    public ResponseEntity<?> handleOrderFailedException(OrderFailedException e){
        logger.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                new UserErrorMessage("Order couldn't be fulfilled! Please try again!")
        );
    }

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<?> handleDataFormatException(DataFormatException e){
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new UserErrorMessage("Unable to retrieve information!")
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(Exception e){
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new UserErrorMessage("Something went wrong!")
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<UserErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("Already existing email!")
        );
    }

    @ExceptionHandler({IOException.class, JsonProcessingException.class})
    public ResponseEntity<UserErrorMessage> handleIOExceptionAndJsonProcessingException(Exception e){
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new UserErrorMessage("Something went wrong! Couldn't save product! Please try again!")
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<UserErrorMessage> handleJsonValidationException(ValidationException e){
        logger.error(e.getErrorMessage(), e);
        String errorMessage;
        if(!e.getCausingExceptions().isEmpty()){
            errorMessage = e.getCausingExceptions().stream()
                    .map(violatedSchema -> (String) violatedSchema.getViolatedSchema().getUnprocessedProperties().get("errorMessage"))
                    .collect(Collectors.joining(", "));
        } else{
            errorMessage = (String) e.getViolatedSchema().getUnprocessedProperties().get("errorMessage");
        }


        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new UserErrorMessage("Incorrect inputs: " + errorMessage)
        );
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<?> handlePaymentException(StripeException e){
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                new UserErrorMessage("Payment process failed! Please try it again!")
        );
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<?> handleInvalidInputException(InvalidInputException e){
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new UserErrorMessage(e.getMessage())
        );
    }
}
