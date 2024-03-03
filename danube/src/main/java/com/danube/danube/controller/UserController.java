package com.danube.danube.controller;

import com.danube.danube.controller.advice.Advice;
import com.danube.danube.model.dto.JwtResponse;
import com.danube.danube.model.dto.UserLoginDTO;
import com.danube.danube.model.dto.UserRegistrationDTO;
import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final Advice controllerAdvice;

    @Autowired
    public UserController(UserService userService, Advice controllerAdvice, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.controllerAdvice = controllerAdvice;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRegistrationDTO userRegistrationDTO){
        try{
            userService.saveUser(userRegistrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO){
        try{
            JwtResponse jwtResponse = userService.loginUser(userLoginDTO);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }
}
