package com.danube.danube.utility.validation.request.user;

import com.danube.danube.custom_exception.input.InvalidInputException;
import com.danube.danube.model.dto.user.PasswordUpdateDTO;
import com.danube.danube.model.dto.user.UserLoginDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.dto.user.UserUpdateDTO;
import com.danube.danube.utility.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRequestValidatorImpl implements UserRequestValidator{
    private final Validator validator;

    @Autowired
    public UserRequestValidatorImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void validateUserRegistrationDTO(UserRegistrationDTO userRegistrationDTO){
        validator.validateTextInputIsNotEmpty(userRegistrationDTO.password());
        validator.validateTextInputIsNotEmpty(userRegistrationDTO.email());
        validator.validateTextInputIsNotEmpty(userRegistrationDTO.firstName());
        validator.validateTextInputIsNotEmpty(userRegistrationDTO.lastName());
        validator.validateEmailFormat(userRegistrationDTO.email());
    }

    @Override
    public void validateUserUpdateDTO(UserUpdateDTO userUpdateDTO){
        validator.validateTextInputIsNotEmpty(userUpdateDTO.email());
        validator.validateTextInputIsNotEmpty(userUpdateDTO.firstName());
        validator.validateTextInputIsNotEmpty(userUpdateDTO.lastName());
        validator.validateEmailFormat(userUpdateDTO.email());
    }

    @Override
    public void validateUserLoginDTO(UserLoginDTO userLoginDTO){
        validator.validateTextInputIsNotEmpty(userLoginDTO.email());
        validator.validateTextInputIsNotEmpty(userLoginDTO.email());
        validator.validateEmailFormat(userLoginDTO.email());
    }

    @Override
    public void validatePasswordUpdateDTO(PasswordUpdateDTO passwordUpdateDTO){
        validator.validateTextInputIsNotEmpty(passwordUpdateDTO.newPassword());
        validator.validateTextInputIsNotEmpty(passwordUpdateDTO.reenterPassword());
        validator.validateTextInputIsNotEmpty(passwordUpdateDTO.currentPassword());

        if(!passwordUpdateDTO.newPassword().equals(passwordUpdateDTO.reenterPassword())){
            throw new InvalidInputException("New password and re-enter password doesn't match!");
        }
    }
}
