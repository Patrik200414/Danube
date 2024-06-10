package com.danube.danube.utility.converter.user;

import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class UserConverterImpl implements UserConverter{

    @Override
    public UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder){
        UserEntity user = new UserEntity();
        user.setFirstName(userRegistrationDTO.firstName());
        user.setLastName(userRegistrationDTO.lastName());
        user.setEmail(userRegistrationDTO.email());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.password()));
        user.setRoles(Set.of(Role.ROLE_CUSTOMER));

        return user;
    }

    @Override
    public JwtResponse generateJwtResponse(UserEntity user, JwtUtils jwtUtils){
        String jwtToken = jwtUtils.generateJwtToken(user.getEmail());
        List<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .toList();

        return new JwtResponse(
                jwtToken,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getId(),
                roles
        );
    }
}
