package com.Registration.UserRegister.controller;

import com.Registration.UserRegister.model.UserRegistrationRequest;
import com.Registration.UserRegister.model.UserRegistrationResponse;
import com.Registration.UserRegister.service.IPGeolocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Stream;

@RestController
@Slf4j
@RequestMapping("/api")
public class UserController {
    private final IPGeolocationService ipGeolocationService;

    @Autowired
    public UserController(IPGeolocationService ipGeolocationService) {
        this.ipGeolocationService = ipGeolocationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        // Validate inputs
        if (isNullorEmpty(request.getUsername(), request.getPassword(), request.getIp())) {
            log.info("Request is invalid");
            return badRequestResponse("All parameters are required.");
        }

        if (isInvalidPassword(request.getPassword())) {
            log.info("password is invalid");
            return badRequestResponse("Password must be at least 8 characters long and contain at least one number, one uppercase letter, and one special character from '_#$%.'");
        }

        if (!ipGeolocationService.isCanadianIP(request.getIp())) {
            log.info("IP is not Canadian");
            return badRequestResponse("User is not eligible to register. Only users from Canada can register.");
        }

        // If all validations pass, generate a random UUID and return response
        String uuid = UUID.randomUUID().toString();
        String cityName = ipGeolocationService.getCityNameFromIP(request.getIp());
        String welcomeMessage = "Welcome, " + request.getUsername() + "! You are from " + cityName;

        return ResponseEntity.ok(new UserRegistrationResponse(uuid, welcomeMessage));
    }

    private boolean isNullorEmpty(String... strings) {
        return Stream.of(strings).anyMatch(string -> string == null || string.isEmpty());
    }

    private boolean isInvalidPassword(String password) {
        return password.length() < 8 ||
                !password.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[_#$%.]).+$");
    }

    private ResponseEntity<UserRegistrationResponse> badRequestResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserRegistrationResponse(message));
    }
}
