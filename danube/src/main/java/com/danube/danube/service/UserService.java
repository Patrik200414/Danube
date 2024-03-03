package com.danube.danube.service;

import com.danube.danube.custom_exception.EmailNotFoundException;
import com.danube.danube.custom_exception.InputTooShortException;
import com.danube.danube.custom_exception.InvalidEmailFormatException;
import com.danube.danube.custom_exception.RegistrationFieldNullException;
import com.danube.danube.model.dto.JwtResponse;
import com.danube.danube.model.dto.UserLoginDTO;
import com.danube.danube.model.dto.UserRegistrationDTO;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.service.utility.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {
    public static final int MIN_LENGTH_NAME = 2;
    public static final int MIN_LENGTH_PASSWORD = 6;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public void saveUser(UserRegistrationDTO userRegistrationDTO){
        registrationValidator(userRegistrationDTO);
        UserEntity user = Converter.convertUserRegistrationDTOToUserEntity(userRegistrationDTO, passwordEncoder);
        userRepository.save(user);
    }

    public JwtResponse loginUser(UserLoginDTO userLoginDTO){
        validateEmail(userLoginDTO.email());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.email(), userLoginDTO.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserEntity user = userRepository.findByEmail(userLoginDTO.email()).orElseThrow(() -> new EmailNotFoundException(userLoginDTO.email()));

        return new JwtResponse(jwt, user.getFirstName(), roles);
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

        if(userRegistrationDTO.firstName().length() < MIN_LENGTH_NAME){
            throw new InputTooShortException("first name", MIN_LENGTH_NAME);
        } else if(userRegistrationDTO.password().length() < MIN_LENGTH_PASSWORD){
            throw new InputTooShortException("password", MIN_LENGTH_PASSWORD);
        } else if(userRegistrationDTO.lastName().length() < MIN_LENGTH_NAME){
            throw new InputTooShortException("last name", MIN_LENGTH_NAME);
        }

        validateEmail(userRegistrationDTO.email());
    }

    private void validateEmail(String email){
        if(!Pattern.compile(".+\\@.+\\..+").matcher(email).matches()){
            throw new InvalidEmailFormatException();
        }
    }
}
