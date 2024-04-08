package com.danube.danube.controller;

import com.danube.danube.controller.advice.Advice;
import com.danube.danube.custom_exception.user.UserEntityPasswordMissMatchException;
import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.*;
import com.danube.danube.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO){
        JwtResponse jwtResponse = userService.loginUser(userLoginDTO);
        return ResponseEntity.ok(jwtResponse);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable long id){
        JwtResponse jwtResponse = userService.addSellerRoleToUser(id);
        return ResponseEntity.ok(jwtResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UserUpdateDTO userUpdateDTO){
        JwtResponse jwtResponse = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(jwtResponse);
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable long id, @RequestBody PasswordUpdateDTO passwordUpdateDTO){
        userService.updatePassword(id, passwordUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyProfile(@RequestBody UserVerificationDTO userVerificationDTO){
        boolean result = userService.verifyUser(userVerificationDTO);

        if(!result){
            throw new UserEntityPasswordMissMatchException();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


}
