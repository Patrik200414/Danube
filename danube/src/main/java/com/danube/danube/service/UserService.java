package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.*;
import com.danube.danube.custom_exception.user.InvalidUserCredentialsException;
import com.danube.danube.custom_exception.user.NotMatchingCurrentPasswordException;
import com.danube.danube.custom_exception.user.NotMatchingNewPasswordAndReenterPasswordException;
import com.danube.danube.model.dto.user.*;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.utility.converter.user.UserConverter;
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
import java.util.regex.Pattern;

@Service
public class UserService {
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MIN_LENGTH_PASSWORD = 6;
    public static final int MINUTE_TO_MILLISECONDS = 60000;
    @Value("${danube.app.jwtVerificationTimeInMinutes}")
    private int JWT_VERIFICATION_TIME_IN_MINUTES;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserConverter userConverter;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userConverter = userConverter;
    }

    public void saveUser(UserRegistrationDTO userRegistrationDTO){
        registrationValidator(userRegistrationDTO);
        UserEntity user = userConverter.convertUserRegistrationDTOToUserEntity(userRegistrationDTO, passwordEncoder);
        userRepository.save(user);
    }

    public UserEntity loginUser(UserLoginDTO userLoginDTO){
        validateEmail(userLoginDTO.email());
        verifyUserCredentials(userLoginDTO.email(), userLoginDTO.password());
        return userRepository.findByEmail(userLoginDTO.email()).orElseThrow(() -> new EmailNotFoundException(userLoginDTO.email()));
    }

    public UserEntity addSellerRoleToUser(long id, String token){
        UserEntity user = userRepository.findById(id).orElseThrow(NonExistingUserException::new);
        validateUserByThereRequestTokenInformation(token, user);

        Set<Role> userRoles = user.getRoles();
        userRoles.add(Role.ROLE_SELLER);

        user.setRoles(userRoles);

        return userRepository.save(user);
    }

    @Transactional
    public UserEntity updateUser(long id, UserUpdateDTO userUpdateDTO, String token){
        validateEmail(userUpdateDTO.email());
        validateFirstNameAndLastName(userUpdateDTO.firstName(), userUpdateDTO.lastName());

        UserEntity user = userRepository.findById(id).orElseThrow(NonExistingUserException::new);
        String emailFromJwtToken = jwtUtils.getEmailFromJwtToken(token);

        validateUserByThereRequestTokenInformation(emailFromJwtToken, user);

        user.setEmail(userUpdateDTO.email());
        user.setFirstName(userUpdateDTO.firstName());
        user.setLastName(userUpdateDTO.lastName());

        return userRepository.save(user);
    }

    @Transactional
    public void updatePassword(long id, PasswordUpdateDTO passwordUpdateDTO, String token){
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

    public UserEntity authenticateUser(String email, String password){
        Authentication authentication = verifyUserCredentials(email, password);

        if(!authentication.isAuthenticated()){
            throw new InvalidUserCredentialsException();
        }

        return userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
    }


    private Authentication verifyUserCredentials(String email, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private void validatePasswordUpdate(PasswordUpdateDTO passwordUpdateDTO, UserEntity user, String toke) {
        validateUserByThereRequestTokenInformation(toke, user);

        if(!passwordEncoder.matches(passwordUpdateDTO.currentPassword(), user.getPassword())){
            throw new NotMatchingCurrentPasswordException();
        }

        if(!passwordUpdateDTO.newPassword().equals(passwordUpdateDTO.reenterPassword())){
            throw new NotMatchingNewPasswordAndReenterPasswordException();
        }
    }


    private void registrationValidator(UserRegistrationDTO userRegistrationDTO){
        validateFieldNotEmpty(userRegistrationDTO);
        validateFirstNameAndLastName(userRegistrationDTO.firstName(), userRegistrationDTO.lastName());
        validatePasswordLength(userRegistrationDTO.password());

        validateEmail(userRegistrationDTO.email());
    }

    private static void validatePasswordLength(String password) {
        if (password.length() < MIN_LENGTH_PASSWORD) {
            throw new InputTooShortException("password", MIN_LENGTH_PASSWORD);
        }
    }

    private void validateFieldNotEmpty(UserRegistrationDTO userRegistrationDTO) {
        if(userRegistrationDTO.email() == null || userRegistrationDTO.email().isEmpty()){
            throw new RegistrationFieldNullException("email");
        } else if(userRegistrationDTO.firstName() == null || userRegistrationDTO.firstName().isEmpty()){
            throw new RegistrationFieldNullException("first name");
        } else if(userRegistrationDTO.lastName() == null || userRegistrationDTO.lastName().isEmpty()){
            throw new RegistrationFieldNullException("last name");
        } else if(userRegistrationDTO.password() == null || userRegistrationDTO.password().isEmpty()){
            throw new RegistrationFieldNullException("password");
        }
    }


    private void validateFirstNameAndLastName(String firstName, String lastName){
        if(firstName.length() < MIN_LENGTH_NAME){
            throw new InputTooShortException("first name", MIN_LENGTH_NAME);
        } else if(lastName.length() < MIN_LENGTH_NAME){
            throw new InputTooShortException("last name", MIN_LENGTH_NAME);
        }
    }

    private void validateEmail(String email){
        if(!Pattern.compile(".+\\@.+\\..+").matcher(email).matches()){
            throw new InvalidEmailFormatException();
        }
    }

    private void validateUserByThereRequestTokenInformation(String token, UserEntity user){
        String emailFromJwtToken = jwtUtils.getEmailFromJwtToken(token);

        if(!Objects.equals(user.getEmail(), emailFromJwtToken)){
            throw new InvalidUserCredentialsException();
        }
    }
}
