package com.danube.danube.service;

import com.danube.danube.custom_exception.InputTooShortException;
import com.danube.danube.custom_exception.InvalidEmailFormatException;
import com.danube.danube.custom_exception.RegistrationFieldNullException;
import com.danube.danube.model.dto.UserRegistrationDTO;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.service.utility.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(UserRegistrationDTO userRegistrationDTO){
        registrationValidator(userRegistrationDTO);
        UserEntity user = Converter.convertUserRegistrationDTOToUserEntity(userRegistrationDTO, passwordEncoder);
        userRepository.save(user);
    }

    private void registrationValidator(UserRegistrationDTO userRegistrationDTO){
        if(userRegistrationDTO.email() == null){
            throw new RegistrationFieldNullException("email");
        } else if(userRegistrationDTO.firstName() == null){
            throw new RegistrationFieldNullException("first name");
        } else if(userRegistrationDTO.lastName() == null){
            throw new RegistrationFieldNullException("last name");
        } else if(userRegistrationDTO.password() == null){
            throw new RegistrationFieldNullException("password");
        }

        if(userRegistrationDTO.firstName().length() < 2){
            throw new InputTooShortException("first name");
        } else if(userRegistrationDTO.password().length() < 2){
            throw new InputTooShortException("password");
        } else if(userRegistrationDTO.lastName().length() < 2){
            throw new InputTooShortException("last name");
        } else if(!userRegistrationDTO.email().matches("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}")){
            throw new InvalidEmailFormatException();
        }
    }
}
