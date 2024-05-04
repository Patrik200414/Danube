package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.*;
import com.danube.danube.custom_exception.user.NotMatchingCurrentPasswordException;
import com.danube.danube.custom_exception.user.NotMatchingNewPasswordAndReenterPasswordException;
import com.danube.danube.custom_exception.user.NotMatchingUserAndUpdateUserIdException;
import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.PasswordUpdateDTO;
import com.danube.danube.model.dto.user.UserLoginDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.dto.user.UserUpdateDTO;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.utility.converter.Converter;
import com.danube.danube.utility.converter.ConverterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    void loginUser_WithInvalidEmail_ShouldThrowInvalidEmailFormatException() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("NotValidEmailFormat", "Password");
        assertThrowsExactly(InvalidEmailFormatException.class, () -> userService.loginUser(userLoginDTO));
    }

    @Test
    void loginUser_WithValidLogin_ShouldReturnExpectedJwtResponse(){
        String expectedJwtToken = "JWT_TOKEN";
        UserLoginDTO userLoginDTO = new UserLoginDTO("test@gmail.com", "Password");
        UserEntity user = new UserEntity(
                1,
                "First",
                "Last",
                "test@gmail.com",
                "Password",
                Set.of(Role.ROLE_CUSTOMER),
                List.of(),
                orders);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("CUSTOMER");
        User principal = new User(
                userLoginDTO.email(),
                userLoginDTO.password(),
                true,
                true,
                true,
                true,
                List.of(grantedAuthority)
        );


        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, userLoginDTO.password(), Set.of(grantedAuthority));

        when(authenticationManagerMock.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.email(), userLoginDTO.password())
        )).thenReturn(
                authentication
        );
        when(jwtUtilsMock.generateJwtToken(authentication)).thenReturn(expectedJwtToken);
        when(userRepositoryMock.findByEmail(userLoginDTO.email())).thenReturn(
                Optional.of(user)
        );


        JwtResponse response = userService.loginUser(userLoginDTO);
        JwtResponse expected = new JwtResponse(
                expectedJwtToken,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getId(),
                List.of("CUSTOMER")
        );


        assertEquals(response, expected);
    }

    @Test
    void loginUser_WithInvalidEmailFormat_ShouldThrowInvalidEmailFormatException(){
        UserLoginDTO userLoginDTO = new UserLoginDTO(
                "notvalidEmail",
                "Password"
        );


        assertThrowsExactly(InvalidEmailFormatException.class, () -> userService.loginUser(userLoginDTO));
    }

    @Test
    void loginUser_WithInvalidCredentialEmail_ShouldThrowEmailNotFoundException(){
        String expectedJwtToken = "JWT_TOKEN";
        UserLoginDTO userLoginDTO = new UserLoginDTO("test@gmail.com", "Password");
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("CUSTOMER");
        User principal = new User(
                userLoginDTO.email(),
                userLoginDTO.password(),
                true,
                true,
                true,
                true,
                List.of(grantedAuthority)
        );


        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, userLoginDTO.password(), Set.of(grantedAuthority));
        when(authenticationManagerMock.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.email(), userLoginDTO.password())
        )).thenReturn(
                authentication
        );
        when(jwtUtilsMock.generateJwtToken(authentication)).thenReturn(expectedJwtToken);
        when(userRepositoryMock.findByEmail(userLoginDTO.email())).thenReturn(
                Optional.empty()
        );


         assertThrowsExactly(EmailNotFoundException.class, () -> userService.loginUser(userLoginDTO));
    }

    @Test
    void addSellerRoleToUser_WithUserRepositoryReturnEmptyOptional_ShouldThrowNonExistingUserException() {
        when(userRepositoryMock.findById(1L)).thenReturn(
                Optional.empty()
        );


        assertThrowsExactly(NonExistingUserException.class, () -> userService.addSellerRoleToUser(1));
    }


    @Test
    void updateUser_WithDifferentUserIdAndUpdateId_ShouldReturnNotMatchingUserAndUpdateUserIdException() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                "test@gmail.com",
                "First",
                "Last",
                1
        );


        assertThrowsExactly(NotMatchingUserAndUpdateUserIdException.class, () -> userService.updateUser(2, userUpdateDTO));
    }

    @Test
    void updateUser_WithUserRepositoryReturnsEmptyOptional_ShouldThrowNonExistingUserException() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                "test@gmail.com",
                "First",
                "Last",
                1
        );

        when(userRepositoryMock.findById(userUpdateDTO.userId())).thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> userService.updateUser(1, userUpdateDTO));
    }

    @Test
    void updateUser_WithNewEmailFormatInvalid_ShouldThrowInvalidEmailFormatException() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                "ThisIsNotValidEmail",
                "First",
                "Last",
                1
        );

        assertThrowsExactly(InvalidEmailFormatException.class, () -> userService.updateUser(1, userUpdateDTO));
    }

    @Test
    void updateUser_WithFirstNameIsTooShort_ShouldThrowInputTooShortException() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                "test@gmail.com",
                "F",
                "Last",
                1
        );

        assertThrowsExactly(InputTooShortException.class, () -> userService.updateUser(1, userUpdateDTO));
    }

    @Test
    void updateUser_WithValidUserUpdateDTO_ShouldReturnJwtResponse(){
        String expectedJwtToken = "EXPECTED_JWT_TOKEN";

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                "test@gmail.com",
                "First",
                "Last",
                1
        );

        UserEntity oldUser = new UserEntity(
                1,
                "Oldfirst",
                "Oldlast",
                "oldTest@gmail.com",
                "OldPassword",
                Set.of(Role.ROLE_CUSTOMER),
                List.of(),
                orders);

        when(userRepositoryMock.findById(1L)).thenReturn(
                Optional.of(oldUser)
        );

        when(userRepositoryMock.save(oldUser)).thenReturn(
                new UserEntity(
                        userUpdateDTO.userId(),
                        userUpdateDTO.firstName(),
                        userUpdateDTO.lastName(),
                        userUpdateDTO.email(),
                        "OldPassword",
                        Set.of(Role.ROLE_CUSTOMER),
                        List.of(),
                        orders)
        );

        when(jwtUtilsMock.generateJwtToken(userUpdateDTO.email())).thenReturn(expectedJwtToken);

        JwtResponse result = userService.updateUser(1, userUpdateDTO);
        JwtResponse expected = new JwtResponse(
                expectedJwtToken,
                userUpdateDTO.firstName(),
                userUpdateDTO.lastName(),
                userUpdateDTO.email(),
                userUpdateDTO.userId(),
                List.of(String.valueOf(Role.ROLE_CUSTOMER))
        );

        assertEquals(expected, result);
    }

    @Test
    void updatePassword_WithUserRepositoryReturnsEmptyOptional_ShouldThrowNonExistingUserException() {
        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO(
                "Password",
                "NewPassword",
                "NewPassword"
        );

        when(userRepositoryMock.findById(1L)).thenReturn(
                Optional.empty()
        );

        assertThrowsExactly(NonExistingUserException.class, () -> userService.updatePassword(1L, passwordUpdateDTO));
    }


    @Test
    void updatePassword_WithNotMatchingCurrentPassword_ShouldThrowNotMatchingCurrentPasswordException() {
        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO(
                "Password",
                "NewPassword",
                "NewPassword"
        );
        UserEntity expectedUser = new UserEntity(
                1,
                "First",
                "Last",
                "test@gmail.com",
                "PasswordCurrent",
                Set.of(Role.ROLE_CUSTOMER),
                List.of(),
                orders);


        when(userRepositoryMock.findById(1L)).thenReturn(
                Optional.of(
                        expectedUser
                )
        );
        when(passwordEncoderMock.matches(passwordUpdateDTO.currentPassword(), expectedUser.getPassword())).thenReturn(false);

        assertThrowsExactly(NotMatchingCurrentPasswordException.class, () -> userService.updatePassword(1L, passwordUpdateDTO));
    }

    @Test
    void updatePassword_(){
        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO(
                "Password",
                "NewPassword",
                "WrongNewPassword"
        );
        UserEntity expectedUser = new UserEntity(
                1,
                "First",
                "Last",
                "test@gmail.com",
                "Password",
                Set.of(Role.ROLE_CUSTOMER),
                List.of(),
                orders);


        when(userRepositoryMock.findById(1L)).thenReturn(
                Optional.of(
                        expectedUser
                )
        );
        when(passwordEncoderMock.matches(passwordUpdateDTO.currentPassword(), expectedUser.getPassword())).thenReturn(true);

        assertThrowsExactly(NotMatchingNewPasswordAndReenterPasswordException.class, () -> userService.updatePassword(1, passwordUpdateDTO));
    }
}