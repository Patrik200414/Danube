package com.danube.danube.utility.validation.request.user;

import com.danube.danube.custom_exception.input.InvalidInputException;
import com.danube.danube.model.dto.user.PasswordUpdateDTO;
import com.danube.danube.model.dto.user.UserLoginDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.dto.user.UserUpdateDTO;
import com.danube.danube.utility.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRequestValidatorImpl implements UserRequestValidator{
    public static final int MIN_LENGTH_PASSWORD = 6;
    public static final int MAX_LENGTH_PASSWORD = 255;
    public static final int MIN_LENGTH_NAME_INPUT = 2;
    public static final int MAX_LENGTH_NAME_INPUT = 255;
    private final Validator validator;

    @Autowired
    public UserRequestValidatorImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void validateUserRegistrationDTO(UserRegistrationDTO userRegistrationDTO){
        validator.validateTextInput(userRegistrationDTO.password(), MIN_LENGTH_PASSWORD, MAX_LENGTH_PASSWORD);
        validator.validateTextInput(userRegistrationDTO.firstName(), MIN_LENGTH_NAME_INPUT, MAX_LENGTH_NAME_INPUT);
        validator.validateTextInput(userRegistrationDTO.lastName(), MIN_LENGTH_NAME_INPUT, MAX_LENGTH_NAME_INPUT);
        validator.validateEmailFormat(userRegistrationDTO.email());
    }

    @Override
    public void validateUserUpdateDTO(UserUpdateDTO userUpdateDTO){
        validator.validateTextInput(userUpdateDTO.firstName(), MIN_LENGTH_NAME_INPUT, MAX_LENGTH_NAME_INPUT);
        validator.validateTextInput(userUpdateDTO.lastName(), MIN_LENGTH_NAME_INPUT, MAX_LENGTH_NAME_INPUT);
        validator.validateEmailFormat(userUpdateDTO.email());
    }

    @Override
    public void validateUserLoginDTO(UserLoginDTO userLoginDTO){
        validator.validateEmailFormat(userLoginDTO.email());
    }

    @Override
    public void validatePasswordUpdateDTO(PasswordUpdateDTO passwordUpdateDTO){
        validator.validateTextInput(passwordUpdateDTO.newPassword(), MIN_LENGTH_PASSWORD, MAX_LENGTH_PASSWORD);
        validator.validateTextInput(passwordUpdateDTO.reenterPassword(), MIN_LENGTH_PASSWORD, MAX_LENGTH_PASSWORD);
        validator.validateTextInput(passwordUpdateDTO.currentPassword(), MIN_LENGTH_PASSWORD, MAX_LENGTH_PASSWORD);

        if(!passwordUpdateDTO.newPassword().equals(passwordUpdateDTO.reenterPassword())){
            throw new InvalidInputException("New password and re-enter password doesn't match!");
        }
    }
}
