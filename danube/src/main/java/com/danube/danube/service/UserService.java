package com.danube.danube.service;

import com.danube.danube.custom_exception.input.InvalidInputException;
import com.danube.danube.custom_exception.login_registration.*;
import com.danube.danube.custom_exception.user.InvalidUserCredentialsException;
import com.danube.danube.custom_exception.user.NotMatchingCurrentPasswordException;
import com.danube.danube.custom_exception.user.NotMatchingNewPasswordAndReenterPasswordException;
import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.*;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.utility.converter.user.UserConverter;
import com.danube.danube.utility.validation.Validator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    public static final int MINUTE_TO_MILLISECONDS = 60000;
    @Value("${danube.app.jwtVerificationTimeInMinutes}")
    private int JWT_VERIFICATION_TIME_IN_MINUTES;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserConverter userConverter;
    private final Validator validator;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserConverter userConverter, Validator validator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userConverter = userConverter;
        this.validator = validator;
    }

    public void saveUser(UserRegistrationDTO userRegistrationDTO){
        validateUserRegistrationDTO(userRegistrationDTO);
        String encodedPassword = passwordEncoder.encode(userRegistrationDTO.password());
        UserEntity user = userConverter.convertUserRegistrationDTOToUserEntity(userRegistrationDTO, encodedPassword);
        userRepository.save(user);
    }

    public JwtResponse loginUser(UserLoginDTO userLoginDTO){
        validateUserLoginDTO(userLoginDTO);
        verifyUserCredentials(userLoginDTO.email(), userLoginDTO.password());
        UserEntity user = userRepository.findByEmail(userLoginDTO.email()).orElseThrow(() -> new EmailNotFoundException(userLoginDTO.email()));
        return userConverter.generateJwtResponse(user, jwtUtils);
    }

    public JwtResponse addSellerRoleToUser(UUID id, String token){
        UserEntity user = userRepository.findById(id).orElseThrow(NonExistingUserException::new);
        validateUserByThereRequestTokenInformation(token, user);

        Set<Role> userRoles = user.getRoles();

        if(!userRoles.contains(Role.ROLE_SELLER)){
            userRoles.add(Role.ROLE_SELLER);
            user.setRoles(userRoles);
            UserEntity savedUser = userRepository.save(user);
            return userConverter.generateJwtResponse(savedUser, jwtUtils);
        }

        return userConverter.generateJwtResponse(user, jwtUtils);
    }

    @Transactional
    public JwtResponse updateUser(UUID id, UserUpdateDTO userUpdateDTO, String token){
        validateUserUpdateDTO(userUpdateDTO);
        UserEntity user = userRepository.findById(id).orElseThrow(NonExistingUserException::new);

        validateUserByThereRequestTokenInformation(token, user);

        user.setEmail(userUpdateDTO.email());
        user.setFirstName(userUpdateDTO.firstName());
        user.setLastName(userUpdateDTO.lastName());


        UserEntity savedUser = userRepository.save(user);
        return userConverter.generateJwtResponse(savedUser, jwtUtils);
    }

    @Transactional
    public void updatePassword(UUID id, PasswordUpdateDTO passwordUpdateDTO, String token){
        validatePasswordUpdateDTO(passwordUpdateDTO);
        UserEntity user = userRepository.findById(id).orElseThrow(NonExistingUserException::new);

        validatePasswordUpdate(passwordUpdateDTO, user, token);
        user.setPassword(passwordEncoder.encode(passwordUpdateDTO.newPassword()));
        userRepository.save(user);
    }

    public boolean verifyUser(String token){
        long tokenIssuedAt = jwtUtils.getIssuedAtMilliseconds(token);
        long currDate = new Date().getTime();
        long difference = currDate - tokenIssuedAt;
        int differenceInMinutes = Math.round((float) difference / MINUTE_TO_MILLISECONDS);

        return JWT_VERIFICATION_TIME_IN_MINUTES > differenceInMinutes;
    }

    public JwtResponse authenticateUser(String email, String password){
        Authentication authentication = verifyUserCredentials(email, password);

        if(!authentication.isAuthenticated()){
            throw new InvalidUserCredentialsException();
        }

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
        return userConverter.generateJwtResponse(user, jwtUtils);
    }


    private Authentication verifyUserCredentials(String email, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private void validatePasswordUpdate(PasswordUpdateDTO passwordUpdateDTO, UserEntity user, String toke) {
        validatePasswordUpdateDTO(passwordUpdateDTO);
        validateUserByThereRequestTokenInformation(toke, user);

        if(!passwordEncoder.matches(passwordUpdateDTO.currentPassword(), user.getPassword())){
            throw new NotMatchingCurrentPasswordException();
        }

        if(!passwordUpdateDTO.newPassword().equals(passwordUpdateDTO.reenterPassword())){
            throw new NotMatchingNewPasswordAndReenterPasswordException();
        }
    }

    private void validateUserByThereRequestTokenInformation(String token, UserEntity user){
        String emailFromJwtToken = jwtUtils.getEmailFromJwtToken(token);

        if(!Objects.equals(user.getEmail(), emailFromJwtToken)){
            throw new InvalidUserCredentialsException();
        }
    }

    private void validateUserRegistrationDTO(UserRegistrationDTO userRegistrationDTO){
        validator.validateTextInputIsNotEmpty(userRegistrationDTO.password());
        validator.validateTextInputIsNotEmpty(userRegistrationDTO.email());
        validator.validateTextInputIsNotEmpty(userRegistrationDTO.firstName());
        validator.validateTextInputIsNotEmpty(userRegistrationDTO.lastName());
        validator.validateEmailFormat(userRegistrationDTO.email());
    }

    private void validateUserUpdateDTO(UserUpdateDTO userUpdateDTO){
        validator.validateTextInputIsNotEmpty(userUpdateDTO.email());
        validator.validateTextInputIsNotEmpty(userUpdateDTO.firstName());
        validator.validateTextInputIsNotEmpty(userUpdateDTO.lastName());
        validator.validateEmailFormat(userUpdateDTO.email());
    }

    private void validateUserLoginDTO(UserLoginDTO userLoginDTO){
        validator.validateTextInputIsNotEmpty(userLoginDTO.email());
        validator.validateTextInputIsNotEmpty(userLoginDTO.email());
        validator.validateEmailFormat(userLoginDTO.email());
    }

    private void validatePasswordUpdateDTO(PasswordUpdateDTO passwordUpdateDTO){
        validator.validateTextInputIsNotEmpty(passwordUpdateDTO.newPassword());
        validator.validateTextInputIsNotEmpty(passwordUpdateDTO.reenterPassword());
        validator.validateTextInputIsNotEmpty(passwordUpdateDTO.currentPassword());

        if(!passwordUpdateDTO.newPassword().equals(passwordUpdateDTO.reenterPassword())){
            throw new InvalidInputException("New password and re-enter password doesn't match!");
        }
    }
}
