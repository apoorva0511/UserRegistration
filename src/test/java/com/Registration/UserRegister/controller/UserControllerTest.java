package com.Registration.UserRegister.controller;

import com.Registration.UserRegister.model.UserRegistrationRequest;
import com.Registration.UserRegister.model.UserRegistrationResponse;
import com.Registration.UserRegister.service.IPGeolocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private IPGeolocationService ipGeolocationService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterUser_ValidRequest() {
        UserRegistrationRequest request = new UserRegistrationRequest("john_doe", "P#ssw0rd123", "123.456.789.10");
        when(ipGeolocationService.isCanadianIP("123.456.789.10")).thenReturn(true);
        when(ipGeolocationService.getCityNameFromIP("123.456.789.10")).thenReturn("Toronto");

        ResponseEntity<?> responseEntity = userController.registerUser(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserRegistrationResponse responseBody = (UserRegistrationResponse) responseEntity.getBody();
        assertEquals("Welcome, john_doe! You are from Toronto", responseBody.getMessage());
    }
    @Test
    public void testRegisterUser_MissingParameters() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        ResponseEntity<?> responseEntity = userController.registerUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("All parameters are required.", responseEntity.getBody());
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("john_doe");
        request.setIp("123.456.789.10");
        request.setPassword("password");
        ResponseEntity<?> responseEntity = userController.registerUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Password must be at least 8 characters long and contain at least one number, one uppercase letter, and one special character from '_#$%.'", responseEntity.getBody());
    }
    @Test
    public void testRegisterUser_NonCanadianIP() {
        when(ipGeolocationService.isCanadianIP(anyString())).thenReturn(false);
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("john_doe");
        request.setPassword("P#ssw0rd123");
        request.setIp("123.456.789.10");
        ResponseEntity<?> responseEntity = userController.registerUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User is not eligible to register. Only users from Canada can register.", responseEntity.getBody());
    }
}
