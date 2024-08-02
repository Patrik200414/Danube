package com.danube.danube.controller;

import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.error.UserErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static  org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class UserControllerIT {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testRegistration_WithInvalidEmailInput_ShouldReturnErrorMessageWithStatus422() throws Exception {
        URI endPointUrl = new URI("/api/user/registration");
        UserRegistrationDTO requestBody = new UserRegistrationDTO(
                "Test",
                "User",
                "invalidEmail",
                "Password"
        );

        String test = "{\n" +
                "    \"firstName\": \"Customer\",\n" +
                "    \"lastName\": \"Customer\",\n" +
                "    \"email\": \"incorrentEmailFormat\",\n" +
                "    \"password\": \"password\"\n" +
                "}";
        UserErrorMessage expectedUserErrorMessage = new UserErrorMessage("Test");


        mockMvc.perform(post(endPointUrl)
                .content(test)
        ).andExpect(
                status().isUnprocessableEntity()
        ).andExpect(content().json(expectedUserErrorMessage.toString()));
    }

    @Test
    void login() {
    }

    @Test
    void updateUserRole() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void updatePassword() {
    }

    @Test
    void verifySeller() {
    }

    @Test
    void verifyProfile() {
    }
}