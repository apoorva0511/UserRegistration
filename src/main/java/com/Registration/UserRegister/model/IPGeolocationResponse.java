package com.Registration.UserRegister.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IPGeolocationResponse {

    private String countryCode;
    private String city;
}
