package com.danube.danube.controller;

import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.error.UserErrorMessage;
import com.google.gson.JsonObject;
import org.h2.util.json.JSONObject;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;

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
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }

    @Test
    void testRegistration_WithTooShortFirstName_ShouldReturnErrorMessageWithStatus422() throws Exception {
        URI endPointUri = new URI("/api/user/registration");

        JsonObject expectedRegistrationRequest = getRegistrationJSONObject(
                "T",
                "User",
                "test@example.com",
                "password"
        );


        JsonObject expectedResponse = new JsonObject();
        expectedResponse.addProperty("errorMessage", "Incorrect inputs: Firstname should contain at least 2 and upmost 255 characters!");


        mockMvc.perform(post(endPointUri)
                .content(expectedRegistrationRequest.toString())
        ).andExpect(
                status().isUnprocessableEntity()
        ).andExpect(content().json(expectedResponse.toString()));
    }

    @Test
    void testRegistration_WithTooShortLastName_ShouldReturnErrorMessageWithStatus422() throws Exception {
        URI endPointUri = new URI("/api/user/registration");

        JsonObject expectedRegistrationRequest = getRegistrationJSONObject(
                "Test",
                "U",
                "test@example.com",
                "password"
        );


        JsonObject expectedResponse = new JsonObject();
        expectedResponse.addProperty("errorMessage", "Incorrect inputs: Lastname should contain at least 2 and upmost 255 characters!");


        mockMvc.perform(post(endPointUri)
                .content(expectedRegistrationRequest.toString())
        ).andExpect(
                status().isUnprocessableEntity()
        ).andExpect(content().json(expectedResponse.toString()));
    }

    @Test
    void testRegistration_WithInvalidEmailInput_ShouldReturnErrorMessageWithStatus422() throws Exception {
        URI endPointUri = new URI("/api/user/registration");

        JsonObject expectedRegistrationRequest = getRegistrationJSONObject(
                "Test",
                "User",
                "invalidEmail",
                "password"
        );


        JsonObject expectedResponse = new JsonObject();
        expectedResponse.addProperty("errorMessage", "Incorrect inputs: Invalid email format!");


        mockMvc.perform(post(endPointUri)
                .content(expectedRegistrationRequest.toString())
        ).andExpect(
                status().isUnprocessableEntity()
        ).andExpect(content().json(expectedResponse.toString()));
    }

    @Test
    void testRegistration_WithInvalidTooShortPassword_ShouldReturnErrorMessageWithStatus422() throws Exception {
        URI endPointUri = new URI("/api/user/registration");

        JsonObject expectedRegistrationRequest = getRegistrationJSONObject(
                "Test",
                "User",
                "test@example.com",
                "pass"
        );


        JsonObject expectedResponse = new JsonObject();
        expectedResponse.addProperty("errorMessage", "Incorrect inputs: Password should contain at least 6 and upmost 255 characters!");


        mockMvc.perform(post(endPointUri)
                .content(expectedRegistrationRequest.toString())
        ).andExpect(
                status().isUnprocessableEntity()
        ).andExpect(content().json(expectedResponse.toString()));
    }

    @Test
    void testRegistration_WithValidRegistrationInformation_ShouldReturnStatus200() throws Exception {
        URI endPointUri = new URI("/api/user/registration");

        JsonObject expectedRegistrationRequest = getRegistrationJSONObject(
                "Test",
                "User",
                "test@example.com",
                "password"
        );

        mockMvc.perform(post(endPointUri)
                .content(expectedRegistrationRequest.toString())
        ).andExpect(status().isOk());
    }


    @Test
    void testRegistration_WithEmailAlreadyExists_ShouldReturnErrorMessageWithStatus409() throws Exception {
        URI endPointUri = new URI("/api/user/registration");

        JsonObject firstExpectedRegistrationObject = getRegistrationJSONObject(
                "Test",
                "User",
                "test@example.com",
                "password"
        );

        mockMvc.perform(post(endPointUri)
                .content(firstExpectedRegistrationObject.toString())
        ).andExpect(status().isOk());

        JsonObject secondExpectedRegistrationObject = getRegistrationJSONObject(
                "Test2",
                "User2",
                "test@example.com",
                "Password"
        );

        JsonObject expectedResponse = getErrorMessageJSONObject("Already existing email!");

        mockMvc.perform(post(endPointUri)
                .content(secondExpectedRegistrationObject.toString())
        ).andExpect(
                status().isConflict()
        ).andExpect(
                content().json(expectedResponse.toString())
        );
    }

    @Test
    void testLogin_WithInvalidEmail_ShouldReturnErrorMessageWithStatus422() throws Exception {
        URI endPointUri = new URI("/api/user/login");

        JsonObject expectedLoginRequest = getLoginRequestJSONObject("invalidEmail", "Password");

        JsonObject expectedResponse = new JsonObject();
        expectedResponse.addProperty("errorMessage", "Incorrect inputs: Invalid email format!");

        mockMvc.perform(post(endPointUri)
                .content(expectedLoginRequest.toString())
        ).andExpect(
                status().isUnprocessableEntity()
        ).andExpect(content().json(expectedResponse.toString()));
    }

    @Test
    void testLogin_WithTooShortPassword_ShouldReturnErrorMessageWithStatus422() throws Exception {
        URI endPointUri = new URI("/api/user/login");

        JsonObject expectedLoginRequest = getLoginRequestJSONObject("test@example.com", "Pass");

        JsonObject expectedResponse = new JsonObject();
        expectedResponse.addProperty("errorMessage", "Incorrect inputs: Password should at least be 6 character long!");

        mockMvc.perform(post(endPointUri)
                .content(expectedLoginRequest.toString())
        ).andExpect(
                status().isUnprocessableEntity()
        ).andExpect(content().json(expectedResponse.toString()));
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

    private JsonObject getRegistrationJSONObject(String firstName, String lastName, String email, String password){
        JsonObject expectedRegistrationRequest = new JsonObject();
        expectedRegistrationRequest.addProperty("firstName", firstName);
        expectedRegistrationRequest.addProperty("lastName", lastName);
        expectedRegistrationRequest.addProperty("email", email);
        expectedRegistrationRequest.addProperty("password", password);

        return expectedRegistrationRequest;
    }

    private JsonObject getLoginRequestJSONObject(String email, String password){
        JsonObject expectedLoginRequest = new JsonObject();
        expectedLoginRequest.addProperty("email", email);
        expectedLoginRequest.addProperty("password", password);

        return expectedLoginRequest;
    }

    private JsonObject getErrorMessageJSONObject(String message){
        JsonObject expectedResponse = new JsonObject();
        expectedResponse.addProperty("errorMessage", message);
        return expectedResponse;
    }
}