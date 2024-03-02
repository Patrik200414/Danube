package com.danube.danube.service.utility;

import com.danube.danube.model.dto.UserRegistrationDTO;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Converter {

    public static UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder){
        UserEntity user = new UserEntity();
        user.setFirstName(userRegistrationDTO.firstName());
        user.setLastName(userRegistrationDTO.lastName());
        user.setEmail(userRegistrationDTO.email());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.password()));
        user.setRoles(Set.of(Role.ROLE_CUSTOMER));

        return user;
    }
}
