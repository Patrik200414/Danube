package com.danube.danube.controller;

import com.danube.danube.controller.advice.Advice;
import com.danube.danube.custom_exception.user.UserEntityPasswordMissMatchException;
import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.UserLoginDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.dto.user.UserVerificationDTO;
import com.danube.danube.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final Advice controllerAdvice;

    @Autowired
    public UserController(UserService userService, Advice controllerAdvice) {
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

    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable long id){
        try{
            JwtResponse jwtResponse = userService.addSellerRoleToUser(id);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyProfile(@RequestBody UserVerificationDTO userVerificationDTO){
        try{
            boolean result = userService.verifyUser(userVerificationDTO);

            if(!result){
                throw new UserEntityPasswordMissMatchException();
            }

            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }


}
