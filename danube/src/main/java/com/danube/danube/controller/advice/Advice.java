package com.danube.danube.controller.advice;

import com.danube.danube.custom_exception.login_registration.InputTooShortException;
import com.danube.danube.custom_exception.login_registration.InvalidEmailFormatException;
import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.login_registration.RegistrationFieldNullException;
import com.danube.danube.custom_exception.order.OrderFailedException;
import com.danube.danube.custom_exception.product.*;
import com.danube.danube.custom_exception.user.*;
import com.danube.danube.model.error.UserErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;


@ControllerAdvice
public class Advice {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<UserErrorMessage> handleDataIntegrityViolationException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("Already existing email!")
        );
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<UserErrorMessage> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserErrorMessage(e.getMessage())
        );
    }

    @ExceptionHandler(RegistrationFieldNullException.class)
    public ResponseEntity<UserErrorMessage> handleRegistrationFieldNullException(RegistrationFieldNullException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserErrorMessage(
                        String.format("%s field is empty! Please enter correct value!", e.getMessage())
                )
        );
    }

    @ExceptionHandler(InputTooShortException.class)
    public ResponseEntity<UserErrorMessage> handleInputTooShortException(InputTooShortException e){
        return ResponseEntity.badRequest().body(
                        handleInputTooShortError(e)
        );
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<UserErrorMessage> handleInvalidEmailFormatException(){
        return ResponseEntity.badRequest().body(
                new UserErrorMessage("Email is invalid! Please enter a valid email address!")
        );
    }

    @ExceptionHandler({BadCredentialsException.class, InvalidUserCredentialsException.class})
    public ResponseEntity<UserErrorMessage> handleBadCredentialsException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new UserErrorMessage("Invalid password! This account has different password!")
        );
    }

    @ExceptionHandler(NonExistingUserException.class)
    public ResponseEntity<UserErrorMessage> handleNonExistingUserException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserErrorMessage("This user doesn't exists!")
        );
    }

    @ExceptionHandler(IncorrectProductObjectFormException.class)
    public ResponseEntity<UserErrorMessage> handleIncorrectProductObjectFormException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("Incorrect data for the selected subcategory! Please correct the information!")
        );
    }

    @ExceptionHandler(UserEntityPasswordMissMatchException.class)
    public ResponseEntity<UserErrorMessage> handleUserEntityPasswordMissMatchException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("Incorrect password for the logged in user!")
        );
    }

    @ExceptionHandler(NonExistingSubcategoryException.class)
    public ResponseEntity<UserErrorMessage> handleNonExistingSubcategoryException(){
        return ResponseEntity.badRequest().body(
                new UserErrorMessage("Non existing subcategory!")
        );
    }

    @ExceptionHandler(NotMatchingUserAndUpdateUserIdException.class)
    public ResponseEntity<UserErrorMessage> handleNotMatchingUserAndUpdateUserIdException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new UserErrorMessage("You can't modify other user's information!")
        );
    }

    @ExceptionHandler(NotMatchingCurrentPasswordException.class)
    public ResponseEntity<UserErrorMessage> handleNotMatchingCurrentPasswordException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("The given current password does not matches!")
        );
    }

    @ExceptionHandler(NotMatchingNewPasswordAndReenterPasswordException.class)
    public ResponseEntity<UserErrorMessage> handleNotMatchingNewPasswordAndReenterPasswordException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("The reentered password does not matches the new password!")
        );
    }

    @ExceptionHandler({IOException.class, JsonProcessingException.class})
    public ResponseEntity<UserErrorMessage> handleIOExceptionAndJsonProcessingException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new UserErrorMessage("Something went wrong! Couldn't save product! Please try again!")
        );
    }

    @ExceptionHandler(NonExistingProductException.class)
    public ResponseEntity<UserErrorMessage> handleNonExistingProductException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserErrorMessage("The selected item is not found!")
        );
    }

    @ExceptionHandler(UserNotSellerException.class)
    public ResponseEntity<UserErrorMessage> handleUserNotSellerException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new UserErrorMessage("The current user don't have seller permission!")
        );
    }

    @ExceptionHandler(MissingImageException.class)
    public ResponseEntity<UserErrorMessage> handleMissingImageException(){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new UserErrorMessage("Missing image parameter! At least one image required!")
        );
    }

    @ExceptionHandler(NonExistingValueException.class)
    public ResponseEntity<UserErrorMessage> handleNonExistingValueException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserErrorMessage("Unable to detect the mentioned value of the product!")
        );
    }

    @ExceptionHandler(IncorrectSellerException.class)
    public ResponseEntity<UserErrorMessage> handleIncorrectSellerException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new UserErrorMessage("You are not the seller of the updatable product! You can only update your products!")
        );
    }

    @ExceptionHandler(ExpiredVerificationTokenException.class)
    public ResponseEntity<?> handleExpiredVerificationTokenException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<?> handlePaymentException(){
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                new UserErrorMessage("Payment process failed! Please try it again!")
        );
    }

    @ExceptionHandler(OrderFailedException.class)
    public ResponseEntity<?> handleOrderFailedException(){
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                new UserErrorMessage("Order couldn't be fulfilled! Please try again!")
        );
    }

    private UserErrorMessage handleInputTooShortError(InputTooShortException e){
        String fieldName = e.getMessage().split("-")[0];
        String minLength = e.getMessage().split("-")[1];
        return new UserErrorMessage(String.format("Input at %s field is too short! Input should be at least %s character long!", fieldName, minLength));
    }
}
