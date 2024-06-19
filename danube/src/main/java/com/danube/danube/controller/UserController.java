package com.danube.danube.controller;

import com.danube.danube.custom_exception.user.ExpiredVerificationTokenException;
import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.*;
import com.danube.danube.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRegistrationDTO userRegistrationDTO){
        userService.saveUser(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody UserLoginDTO userLoginDTO){
        return userService.loginUser(userLoginDTO);
    }

    @PatchMapping("/{id}/role")
    public JwtResponse updateUserRole(@PathVariable UUID id, HttpServletRequest request){
        String jwtToken = getJwtTokenFromBearerToken(request);
        return userService.addSellerRoleToUser(id, jwtToken);
    }

    @PutMapping("/{id}")
    public JwtResponse updateUser(@PathVariable UUID id, @RequestBody UserUpdateDTO userUpdateDTO, HttpServletRequest request){
        String jwtToken = getJwtTokenFromBearerToken(request);
        return userService.updateUser(id, userUpdateDTO, jwtToken);
    }

    @PutMapping("/password/{id}")
    public HttpStatus updatePassword(@PathVariable UUID id, @RequestBody PasswordUpdateDTO passwordUpdateDTO, HttpServletRequest request){
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
        return userService.authenticateUser(userAuthentication.email(), userAuthentication.password());
    }


    private String getJwtTokenFromBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        return bearerToken.split(" ")[1];
    }
}
