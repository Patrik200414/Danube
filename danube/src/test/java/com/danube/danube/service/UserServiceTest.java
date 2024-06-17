package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.*;
import com.danube.danube.custom_exception.user.InvalidUserCredentialsException;
import com.danube.danube.custom_exception.user.NotMatchingCurrentPasswordException;
import com.danube.danube.custom_exception.user.NotMatchingNewPasswordAndReenterPasswordException;
import com.danube.danube.model.dto.user.PasswordUpdateDTO;
import com.danube.danube.model.dto.user.UserLoginDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.dto.user.UserUpdateDTO;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.utility.converter.user.UserConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    UserService userService;
    UserRepository userRepositoryMock;
    PasswordEncoder passwordEncoderMock;
    AuthenticationManager authenticationManagerMock;
    JwtUtils jwtUtilsMock;
    UserConverter userConverterMock;
    String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ5b3VyLWFwcCIsInN1YiI6InRlc3R1c2VyIiwiaWF0IjoxNjI1MjU2MDAwLCJleHAiOjE2MjUyNTY2MDB9.2FhwBLVhsn4M3G3x5h6-1Z09ZogUIr9EHz1dxYaCe0Y";


    @BeforeEach
    public void setUp(){
        userRepositoryMock = mock(UserRepository.class);
        passwordEncoderMock = mock(PasswordEncoder.class);
        authenticationManagerMock = mock(AuthenticationManager.class);
        jwtUtilsMock = mock(JwtUtils.class);
        userConverterMock = mock(UserConverter.class);

        userService = new UserService(
                userRepositoryMock,
                passwordEncoderMock,
                authenticationManagerMock,
                jwtUtilsMock,
                userConverterMock
        );
    }

    @Test
    void testLoginUser_WithInvalidEmail_ThrowsInvalidEmailFormatException() {
        UserLoginDTO registration = new UserLoginDTO("ThisIsInvalidEmail", "Password");

        assertThrowsExactly(InvalidEmailFormatException.class, () -> userService.loginUser(registration));
    }

    @Test
    void testLoginUser_WithNotExistingEmail_ThrowsEmailNotFoundException(){
        UserLoginDTO registration = new UserLoginDTO("test@gmail.com", "Password");
        when(userRepositoryMock.findByEmail(registration.email())).thenReturn(Optional.empty());

        assertThrowsExactly(EmailNotFoundException.class, () -> userService.loginUser(registration));
    }

    @Test
    void testSaveUser_WithEmptyValue_ThrowsRegistrationFieldNullException(){
        UserRegistrationDTO registration = new UserRegistrationDTO(
                "",
                "Last",
                "email@gmail.com",
                "Password"
        );
        assertThrowsExactly(RegistrationFieldNullException.class, () -> userService.saveUser(registration));
    }

    @Test
    void testSaveUser_WithTooShortNameFirstNameConsistOfOneLetter_ThrowsInputTooShortException(){
        UserRegistrationDTO registration = new UserRegistrationDTO(
                "A",
                "User",
                "auser@gmaile.com",
                "Password"
        );
        assertThrowsExactly(InputTooShortException.class, () -> userService.saveUser(registration));
    }

    @Test
    void testSaveUser_WithTooShortPasswordPasswordConsistsOfFiveCharacters_ExpectedInputTooShortException(){
        UserRegistrationDTO registration = new UserRegistrationDTO(
                "First",
                "User",
                "firstuser@gmail.com",
                "Test"
        );
        assertThrowsExactly(InputTooShortException.class, () -> userService.saveUser(registration));
    }

    @Test
    void testSaveUser_WithInvalidEmailFormat_ExpectedInvalidEmailFormatException(){
        UserRegistrationDTO registration = new UserRegistrationDTO(
                "First",
                "User",
                "NotValidEmail",
                "Password"
        );
        assertThrowsExactly(InvalidEmailFormatException.class, () -> userService.saveUser(registration));
    }

    @Test
    void testSaveUser_WithValidUserRegistrationDTO_ShouldExecuteUserRepositorySaveWithExpectedValues(){
        UserRegistrationDTO expectedUserRegistration = new UserRegistrationDTO(
                "Test",
                "User",
                "test@gmail.com",
                "Password"
        );
        String expectedEncodedPassword = anyString();

        UserEntity expectedUserEntity = new UserEntity();
        expectedUserEntity.setFirstName(expectedUserRegistration.firstName());
        expectedUserEntity.setLastName(expectedUserRegistration.lastName());
        expectedUserEntity.setEmail(expectedUserRegistration.email());
        expectedUserEntity.setPassword(expectedEncodedPassword);

        when(passwordEncoderMock.encode(expectedUserRegistration.password()))
                .thenReturn(expectedEncodedPassword);

        when(userConverterMock.convertUserRegistrationDTOToUserEntity(expectedUserRegistration, expectedEncodedPassword))
                .thenReturn(expectedUserEntity);

        userService.saveUser(expectedUserRegistration);

        verify(passwordEncoderMock, times(1)).encode(expectedUserRegistration.password());
        verify(userConverterMock, times(1)).convertUserRegistrationDTOToUserEntity(expectedUserRegistration, expectedEncodedPassword);
        verify(userRepositoryMock, times(1)).save(expectedUserEntity);
    }

    @Test
    void testAddSellerRoleToUser_WithNonExistingUserUserRepositoryReturnsEmptyOptional_ExpectedNonExistingUserException() {
        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> userService.addSellerRoleToUser(1L, mockToken));
    }

    @Test
    void testAddSellerRoleToUser_WithInvalidRequestToken_ExpectedInvalidUserCredentialsException(){
        UserEntity expectedUser = new UserEntity();
        expectedUser.setFirstName("User");
        expectedUser.setLastName("First");
        expectedUser.setEmail("userfirst@gmail.com");
        expectedUser.setId(1);

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedUser));

        when(jwtUtilsMock.getEmailFromJwtToken(mockToken))
                .thenReturn("notthesame@gmail.com");

        assertThrowsExactly(InvalidUserCredentialsException.class, () -> userService.addSellerRoleToUser(1L, mockToken));
    }

    @Test
    void testAddSellerRoleToUser_WithRoleOnlyContainsCustomer_ShouldExecuteUserRepositorySaveWithExpectedValues(){
        Set<Role> expectedRolesAtStart = new HashSet<>();
        expectedRolesAtStart.add(Role.ROLE_CUSTOMER);

        UserEntity expectedUser = new UserEntity();
        expectedUser.setFirstName("User");
        expectedUser.setLastName("First");
        expectedUser.setEmail("userfirst@gmail.com");
        expectedUser.setId(1);
        expectedUser.setRoles(expectedRolesAtStart);

        String expectedToken = "FirstExpectedToken";
        String newExpectedJwt = "SecondExpectedToken";

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedUser));

        when(userRepositoryMock.save(expectedUser))
                .thenReturn(expectedUser);

        when(jwtUtilsMock.generateJwtToken(expectedUser.getEmail()))
                .thenReturn(newExpectedJwt);

        when(jwtUtilsMock.getEmailFromJwtToken(expectedToken))
                .thenReturn(expectedUser.getEmail());

        userService.addSellerRoleToUser(1, expectedToken);
        expectedUser.setRoles(Set.of(Role.ROLE_CUSTOMER, Role.ROLE_SELLER));

        verify(userRepositoryMock, times(1)).save(expectedUser);
        verify(userConverterMock, times(1)).generateJwtResponse(expectedUser, jwtUtilsMock);
    }

    @Test
    void testAddSellerRoleToUser_WithRoleContainsCustomerAndSeller_ShouldNotExecuteUserRepositorySaveWithExpectedValues(){
        Set<Role> expectedRolesAtStart = new HashSet<>();
        expectedRolesAtStart.add(Role.ROLE_CUSTOMER);
        expectedRolesAtStart.add(Role.ROLE_SELLER);

        UserEntity expectedUser = new UserEntity();
        expectedUser.setFirstName("User");
        expectedUser.setLastName("First");
        expectedUser.setEmail("userfirst@gmail.com");
        expectedUser.setId(1);
        expectedUser.setRoles(expectedRolesAtStart);

        String expectedToken = "FirstExpectedToken";
        String newExpectedJwt = "SecondExpectedToken";

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedUser));

        when(userRepositoryMock.save(expectedUser))
                .thenReturn(expectedUser);

        when(jwtUtilsMock.generateJwtToken(expectedUser.getEmail()))
                .thenReturn(newExpectedJwt);

        when(jwtUtilsMock.getEmailFromJwtToken(expectedToken))
                .thenReturn(expectedUser.getEmail());

        userService.addSellerRoleToUser(1, expectedToken);
        expectedUser.setRoles(Set.of(Role.ROLE_CUSTOMER, Role.ROLE_SELLER));

        verify(userRepositoryMock, never()).save(expectedUser);
        verify(userConverterMock, times(1)).generateJwtResponse(expectedUser, jwtUtilsMock);
    }

    @Test
    void testUpdatePassword_WithInCorrectCurrentPassword_ExpectedNotMatchingCurrentPasswordException() {
        String expectedEmail = "userfirst@gmail.com";

        UserEntity expectedUser = new UserEntity();
        expectedUser.setFirstName("User");
        expectedUser.setLastName("First");
        expectedUser.setEmail(expectedEmail);
        expectedUser.setId(1);
        expectedUser.setPassword("Password");

        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO(
                "NotGoodCurrPassword",
                "NewPassword",
                "NewPassword"
        );

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedUser));

        when(jwtUtilsMock.getEmailFromJwtToken(mockToken))
                .thenReturn(expectedEmail);

        when(passwordEncoderMock.matches(passwordUpdateDTO.currentPassword(), expectedUser.getPassword()))
                .thenReturn(false);

        assertThrowsExactly(NotMatchingCurrentPasswordException.class, () -> userService.updatePassword(1L, passwordUpdateDTO, mockToken));
    }

    @Test
    void testUpdateUser_WithInvalidEmail_ShouldThrowInvalidEmailFormatException(){
        UserUpdateDTO expectedUserUpdateDTO = new UserUpdateDTO(
                "InvalidEmailAddress",
                "Test",
                "User"
        );
        String expectedJwtToken = anyString();
        assertThrowsExactly(InvalidEmailFormatException.class, () -> userService.updateUser(1, expectedUserUpdateDTO, expectedJwtToken));
    }

    @Test
    void testUpdateUser_WithFirstNameTooShort_ShouldThrowInputTooShortException(){
        UserUpdateDTO expectedUserUpdateDTO = new UserUpdateDTO(
                "test@gmail.com",
                "T",
                "User"
        );
        String expectedJwtToken = anyString();

        assertThrowsExactly(InputTooShortException.class, () -> userService.updateUser(1, expectedUserUpdateDTO, expectedJwtToken));
    }

    @Test
    void testUpdateUser_WithNonExistingUser_ShouldThrowNonExistingUserException(){
        UserUpdateDTO expectedUserUpdateDTO = new UserUpdateDTO(
                "test@gmail.com",
                "Test",
                "User"
        );

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());

        String expectedJwtToken = anyString();

        assertThrowsExactly(NonExistingUserException.class, () -> userService.updateUser(1, expectedUserUpdateDTO, expectedJwtToken));
    }

    @Test
    void testUpdatePassword_WithUpdatedPasswordNotMatchingWithReenterPassword_ExpectedNotMatchingNewPasswordAndReenterPasswordException(){
        String expectedEmail = "userfirst@gmail.com";

        UserEntity expectedUser = new UserEntity();
        expectedUser.setFirstName("User");
        expectedUser.setLastName("First");
        expectedUser.setEmail(expectedEmail);
        expectedUser.setId(1);
        expectedUser.setPassword("Password");

        PasswordUpdateDTO passwordUpdate = new PasswordUpdateDTO(
                "Password",
                "NewPassword",
                "NotNewPassword"
        );

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedUser));

        when(jwtUtilsMock.getEmailFromJwtToken(mockToken))
                .thenReturn(expectedEmail);

        when(passwordEncoderMock.matches(passwordUpdate.currentPassword(), expectedUser.getPassword()))
                .thenReturn(true);

        assertThrowsExactly(NotMatchingNewPasswordAndReenterPasswordException.class, () -> userService.updatePassword(1, passwordUpdate, mockToken));
    }

    @Test
    void testAuthenticateUser_WithAuthenticatedSetFalse_ExpectedInvalidUserCredentialsException() {
        String expectedEmail = "test@gmail.com";
        String expectedPassword = "testpass";

        Authentication auth = new UsernamePasswordAuthenticationToken(expectedEmail, expectedPassword);
        auth.setAuthenticated(false);

        when(authenticationManagerMock.authenticate(
                new UsernamePasswordAuthenticationToken(expectedEmail, expectedPassword)
        )).thenReturn(auth);

        assertThrowsExactly(InvalidUserCredentialsException.class, () -> userService.authenticateUser(expectedEmail, expectedPassword));
    }

    @Test
    void testAuthenticateUser_WithUserRepositoryFindByEmailReturnsEmptyOptional_ExpectedEmailNotFoundException(){
        String expectedEmail = "test@gmail.com";
        String expectedPassword = "testpass";

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_CUSTOMER");
        Authentication auth = new UsernamePasswordAuthenticationToken(expectedEmail, expectedPassword, List.of(grantedAuthority));

        when(authenticationManagerMock.authenticate(
                new UsernamePasswordAuthenticationToken(expectedEmail, expectedPassword)
        )).thenReturn(auth);

        when(userRepositoryMock.findByEmail(expectedEmail))
                .thenReturn(Optional.empty());

        assertThrowsExactly(EmailNotFoundException.class, () -> userService.authenticateUser(expectedEmail, expectedPassword));
    }

    @Test
    void testVerifyUser_WithTokenIssuedAtOneMinuteAgo_ExpectedTrue(){
        long currTime = new Date().getTime();

        when(jwtUtilsMock.getIssuedAtMilliseconds(mockToken))
                .thenReturn(currTime + 60000);


        assertTrue(userService.verifyUser(mockToken));
    }

    @Test
    void testVerifyUser_WithTokenIssuedAtSixMinutesAgo_ExpectedFalse(){
        long currTime = new Date().getTime();

        when(jwtUtilsMock.getIssuedAtMilliseconds(mockToken))
                .thenReturn(currTime - 3600000);


        assertFalse(userService.verifyUser(mockToken));
    }
}