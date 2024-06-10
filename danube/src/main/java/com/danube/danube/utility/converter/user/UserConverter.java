package com.danube.danube.utility.converter.user;

import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.security.jwt.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserConverter {
    UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder);
    JwtResponse generateJwtResponse(UserEntity user, JwtUtils jwtUtils);
}
