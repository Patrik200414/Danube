package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.InputTooShortException;
import com.danube.danube.custom_exception.login_registration.InvalidEmailFormatException;
import com.danube.danube.custom_exception.login_registration.RegistrationFieldNullException;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.utility.Converter;
import com.danube.danube.utility.ConverterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class UserServiceTest {
    private UserRepository userRepositoryMock;
    private PasswordEncoder passwordEncoderMock;
    private AuthenticationManager authenticationManagerMock;
    private UserService userService;
    private JwtUtils jwtUtilsMock;
    private Converter converter;

    @BeforeEach
    void setup(){
        userRepositoryMock = mock(UserRepository.class);
        passwordEncoderMock = mock(PasswordEncoder.class);
        authenticationManagerMock = mock(AuthenticationManager.class);
        jwtUtilsMock = mock(JwtUtils.class);
        converter = new ConverterImpl();
        userService = new UserService(
                userRepositoryMock,
                passwordEncoderMock,
                authenticationManagerMock,
                jwtUtilsMock,
                converter
        );
    }
    @Test
    void saveUser_WithFirstNameIsEmptyString_ShouldThrowRegistrationFieldNullException() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "",
                "Last",
                "test@gmail.com",
                "Password"
        );

        assertThrowsExactly(RegistrationFieldNullException.class, () -> userService.saveUser(userRegistrationDTO));
    }

    @Test
    void saveUser_WithLastNameIsEmptyString_ShouldThrowRegistrationFieldNullException() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "First",
                "",
                "test@gmail.com",
                "Password"
        );

        assertThrowsExactly(RegistrationFieldNullException.class, () -> userService.saveUser(userRegistrationDTO));
    }


    @Test
    void saveUser_WithEmailIsEmptyString_ShouldThrowRegistrationFieldNullException() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "First",
                "Last",
                "",
                "Password"
        );

        assertThrowsExactly(RegistrationFieldNullException.class, () -> userService.saveUser(userRegistrationDTO));
    }


    @Test
    void saveUser_WithPasswordIsEmptyString_ShouldThrowRegistrationFieldNullException() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "First",
                "Last",
                "test@gmail.com",
                ""
        );

        assertThrowsExactly(RegistrationFieldNullException.class, () -> userService.saveUser(userRegistrationDTO));
    }

    @Test
    void saveUser_WithFirstNameContainsOneChar_ShouldThrowInputTooShortException(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "F",
                "Last",
                "test@gmail.com",
                "password"
        );

        assertThrowsExactly(InputTooShortException.class, () -> userService.saveUser(userRegistrationDTO));
    }

    @Test
    void saveUser_WithLastNameContainsOneChar_ShouldThrowInputTooShortException(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "First",
                "L",
                "test@gmail.com",
                "password"
        );

        assertThrowsExactly(InputTooShortException.class, () -> userService.saveUser(userRegistrationDTO));
    }

    @Test
    void saveUser_WithPasswordContainsOneChar_ShouldThrowInputTooShortException(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "First",
                "Last",
                "test@gmail.com",
                "p"
        );

        assertThrowsExactly(InputTooShortException.class, () -> userService.saveUser(userRegistrationDTO));
    }

    @Test
    void saveUser_WithInvalidEmailFormat_ShouldThrowInvalidEmailFormatException(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "First",
                "Last",
                "thisIsNotValidEmailAddress",
                "password"
        );

        assertThrowsExactly(InvalidEmailFormatException.class, () -> userService.saveUser(userRegistrationDTO));
    }

    @Test
    void loginUser() {
    }

    @Test
    void addSellerRoleToUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void updatePassword() {
    }

    @Test
    void verifyUser() {
    }
}