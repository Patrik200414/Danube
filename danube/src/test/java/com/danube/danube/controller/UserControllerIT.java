package com.danube.danube.controller;

import com.danube.danube.model.user.Role;
import com.danube.danube.security.jwt.JwtUtils;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("dev")
class UserControllerIT {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @MockBean
    private JwtUtils jwtUtilsMock;

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
        createValidUser();
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
    void testLogin_NonExistingEmail_ShouldReturnErrorMessageWithStatus404() throws Exception {
        URI endPointUri = new URI("/api/user/login");

        String expectedEmail = "test@example.com";
        JsonObject expectedLoginRequest = getLoginRequestJSONObject(expectedEmail, "password");

        JsonObject expectedResponse = getErrorMessageJSONObject("Authentication failed!");

        mockMvc.perform(post(endPointUri)
                .content(expectedLoginRequest.toString())
        ).andExpect(
                status().isNotFound()
        ).andExpect(content().json(expectedResponse.toString()));
    }

    @Test
    void testLogin_WithValidLoginRequest_ResponseShouldContainExpectedValuesAndReturnsWithStatus200() throws Exception {
        createValidUser();

        URI endPointUri = new URI("/api/user/login");
        String expectedFirstName = "Test";
        String expectedLastName = "User";
        String expectedJwtToken = "TestToken";
        String expectedLoginEmail = "test@example.com";
        List<String> expectedRoles = List.of(Role.ROLE_CUSTOMER.name());

        JsonObject expectedLoginRequest = getLoginRequestJSONObject(expectedLoginEmail, "password");

        when(jwtUtilsMock.generateJwtToken(expectedLoginEmail))
                .thenReturn(expectedJwtToken);

        mockMvc.perform(post(endPointUri)
                .content(expectedLoginRequest.toString())
        ).andExpect(
                status().isOk()
        ).andExpect(jsonPath("$.jwt", is(expectedJwtToken))
        ).andExpect(jsonPath("$.firstName", is(expectedFirstName))
        ).andExpect(jsonPath("$.lastName", is(expectedLastName))
        ).andExpect(jsonPath("$.email", is(expectedLoginEmail))
        ).andExpect(jsonPath("$.roles", is(expectedRoles)));
    }

    @Test
    void testLogin_WithInvalidPasswordCredentail_ShouldReturnErrorMessageWithInvalidPasswordAndStatus401() throws Exception {
        createValidUser();
        URI endPointUri = new URI("/api/user/login");

        JsonObject expectedLoginRequest = getLoginRequestJSONObject("test@example.com", "NotCorrectPassword");

        JsonObject expectedResponse = getErrorMessageJSONObject("Invalid password! This account has different password!");

        mockMvc.perform(post(endPointUri)
                .content(expectedLoginRequest.toString())
        ).andExpect(
                status().isUnauthorized()
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

    private void createValidUser() throws Exception {
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
}