package com.danube.danube.utility.validation.request.user;

import com.danube.danube.model.dto.user.PasswordUpdateDTO;
import com.danube.danube.model.dto.user.UserLoginDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.dto.user.UserUpdateDTO;

public interface UserRequestValidator {
    void validateUserRegistrationDTO(UserRegistrationDTO userRegistrationDTO);
    void validateUserUpdateDTO(UserUpdateDTO userUpdateDTO);
    void validateUserLoginDTO(UserLoginDTO userLoginDTO);
    void validatePasswordUpdateDTO(PasswordUpdateDTO passwordUpdateDTO);
}
