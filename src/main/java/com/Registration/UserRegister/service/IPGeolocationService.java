package com.Registration.UserRegister.service;

public interface IPGeolocationService {
    boolean isCanadianIP(String ip);
    String getCityNameFromIP(String ip);
}
