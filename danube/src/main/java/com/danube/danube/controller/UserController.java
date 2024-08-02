package com.danube.danube.controller;

import com.danube.danube.custom_exception.user.ExpiredVerificationTokenException;
import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.*;
import com.danube.danube.service.UserService;
import com.danube.danube.utility.json.JsonUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    public final JsonUtility jsonUtility;

    @Autowired
    public UserController(UserService userService, JsonUtility jsonUtility) {
        this.userService = userService;
        this.jsonUtility = jsonUtility;
    }

    @PostMapping("/registration")
    public HttpStatus registration(@RequestBody String registrationInformation) throws IOException {
        UserRegistrationDTO userRegistrationDTO = jsonUtility.validateJson(registrationInformation, UserRegistrationDTO.class);
        userService.saveUser(userRegistrationDTO);
        return HttpStatus.CREATED;
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody String loginInformation) throws IOException {
        UserLoginDTO userLoginDTO1 = jsonUtility.validateJson(loginInformation, UserLoginDTO.class);
        return userService.loginUser(userLoginDTO1);
    }

    @PatchMapping("/{id}/role")
    public JwtResponse updateUserRole(@PathVariable UUID id, HttpServletRequest request){
        String jwtToken = getJwtTokenFromBearerToken(request);
        return userService.addSellerRoleToUser(id, jwtToken);
    }

    @PutMapping("/{id}")
    public JwtResponse updateUser(@PathVariable UUID id, @RequestBody String userUpdateInformation, HttpServletRequest request) throws IOException {
        UserUpdateDTO userUpdateDTO = jsonUtility.validateJson(userUpdateInformation, UserUpdateDTO.class);
        String jwtToken = getJwtTokenFromBearerToken(request);
        return userService.updateUser(id, userUpdateDTO, jwtToken);
    }

    @PutMapping("/password/{id}")
    public HttpStatus updatePassword(@PathVariable UUID id, @RequestBody String passwordUpdateInformation, HttpServletRequest request) throws IOException {
        PasswordUpdateDTO passwordUpdateDTO = jsonUtility.validateJson(passwordUpdateInformation, PasswordUpdateDTO.class);
        String jwtToken = getJwtTokenFromBearerToken(request);
        userService.updatePassword(id, passwordUpdateDTO, jwtToken);
        return HttpStatus.OK;
    }

    @PostMapping("/verify")
    public HttpStatus verifySeller(HttpServletRequest request){
        String jwtToken = getJwtTokenFromBearerToken(request);
        boolean isUserVerified = userService.verifyUser(jwtToken);
        if(!isUserVerified){
            throw new ExpiredVerificationTokenException();
        }
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("/authenticate")
    public JwtResponse verifyProfile(@RequestBody String userAuthenticationInformation) throws IOException {
        UserLoginDTO userAuthentication = jsonUtility.validateJson(userAuthenticationInformation, UserLoginDTO.class);
        return userService.authenticateUser(userAuthentication.email(), userAuthentication.password());
    }


    private String getJwtTokenFromBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        return bearerToken.split(" ")[1];
    }
}
