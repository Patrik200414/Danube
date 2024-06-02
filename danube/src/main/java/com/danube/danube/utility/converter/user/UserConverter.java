package com.danube.danube.utility.converter.user;

import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.user.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserConverter {
    UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder);
}
