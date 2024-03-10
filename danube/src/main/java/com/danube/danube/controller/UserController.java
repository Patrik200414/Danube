package com.danube.danube.controller;

import com.danube.danube.controller.advice.Advice;
import com.danube.danube.model.dto.AuthenticatedUserLoginDTO;
import com.danube.danube.model.dto.JwtResponse;
import com.danube.danube.model.dto.UserLoginDTO;
import com.danube.danube.model.dto.UserRegistrationDTO;
import com.danube.danube.service.CookieService;
import com.danube.danube.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final Advice controllerAdvice;
    private final CookieService cookieService;

    @Autowired
    public UserController(UserService userService, Advice controllerAdvice, CookieService cookieService) {
        this.userService = userService;
        this.controllerAdvice = controllerAdvice;
        this.cookieService = cookieService;
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
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse httpServletResponse){
        try{
            JwtResponse jwtResponse = userService.loginUser(userLoginDTO);
            cookieService.setCookie(httpServletResponse, jwtResponse.jwt());
            AuthenticatedUserLoginDTO authenticatedUserLoginDTO = new AuthenticatedUserLoginDTO(jwtResponse.firstName());
            return ResponseEntity.ok(authenticatedUserLoginDTO);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }


}
