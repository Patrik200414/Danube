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
    public JwtResponse login(@RequestBody UserLoginDTO userLoginDTO){
        return userService.loginUser(userLoginDTO);

    }

    @PutMapping("/{id}/role")
    public JwtResponse updateUserRole(@PathVariable long id){
        return userService.addSellerRoleToUser(id);
    }

    @PutMapping("/{id}")
    public JwtResponse updateUser(@PathVariable long id, @RequestBody UserUpdateDTO userUpdateDTO){
        return userService.updateUser(id, userUpdateDTO);
    }

    @PutMapping("/password/{id}")
    public HttpStatus updatePassword(@PathVariable long id, @RequestBody PasswordUpdateDTO passwordUpdateDTO){
        userService.updatePassword(id, passwordUpdateDTO);
        return HttpStatus.OK;
    }

    @PostMapping("/verify")
    public HttpStatus verifyProfile(@RequestBody UserVerificationDTO userVerificationDTO){
        userService.verifyUser(userVerificationDTO);
        return HttpStatus.ACCEPTED;
    }


}
