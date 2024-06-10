package com.danube.danube.controller;

import com.danube.danube.custom_exception.user.ExpiredVerificationTokenException;
import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.*;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.service.UserService;
import com.danube.danube.utility.converter.user.UserConverter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter, JwtUtils jwtUtils) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRegistrationDTO userRegistrationDTO){
        userService.saveUser(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody UserLoginDTO userLoginDTO){
        UserEntity user = userService.loginUser(userLoginDTO);
        return userConverter.generateJwtResponse(user, jwtUtils);

    }

    @PatchMapping("/{id}/role")
    public JwtResponse updateUserRole(@PathVariable long id, HttpServletRequest request){
        String jwtToken = getJwtTokenFromBearerToken(request);
        UserEntity user = userService.addSellerRoleToUser(id, jwtToken);
        return userConverter.generateJwtResponse(user, jwtUtils);
    }

    @PutMapping("/{id}")
    public JwtResponse updateUser(@PathVariable long id, @RequestBody UserUpdateDTO userUpdateDTO, HttpServletRequest request){
        String jwtToken = getJwtTokenFromBearerToken(request);
        UserEntity user = userService.updateUser(id, userUpdateDTO, jwtToken);
        return userConverter.generateJwtResponse(user, jwtUtils);
    }

    @PutMapping("/password/{id}")
    public HttpStatus updatePassword(@PathVariable long id, @RequestBody PasswordUpdateDTO passwordUpdateDTO, HttpServletRequest request){
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
    public JwtResponse verifyProfile(@RequestBody UserLoginDTO userAuthentication){
        UserEntity user = userService.authenticateUser(userAuthentication.email(), userAuthentication.password());
        return userConverter.generateJwtResponse(user, jwtUtils);
    }


    private String getJwtTokenFromBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        return bearerToken.split(" ")[1];
    }
}
