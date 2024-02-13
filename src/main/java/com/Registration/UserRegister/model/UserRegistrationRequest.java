package com.Registration.UserRegister.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegistrationRequest {

    private String username;
    private String password;
    private String ip;
}
