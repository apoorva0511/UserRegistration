package com.Registration.UserRegister.service;

import com.Registration.UserRegister.model.IPGeolocationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class IPGeolocationServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IPGeolocationServiceImpl ipGeolocationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsCanadianIP_CanadianIP() {

        IPGeolocationResponse response = new IPGeolocationResponse();
        response.setCountryCode("CA");
        when(restTemplate.getForObject(any(String.class), any())).thenReturn(response);
        boolean result = ipGeolocationService.isCanadianIP("100.42.20.12");
        assertTrue(result);
    }

    @Test
    public void testIsCanadianIP_NonCanadianIP() {
        IPGeolocationResponse response = new IPGeolocationResponse();
        response.setCountryCode("US");
        when(restTemplate.getForObject(any(String.class), any())).thenReturn(response);
        boolean result = ipGeolocationService.isCanadianIP("123.456.789.10");
        assertFalse(result);
    }

    @Test
    public void testGetCityNameFromIP_ValidResponse() {
        IPGeolocationResponse response = new IPGeolocationResponse();
        response.setCity("Toronto");
        when(restTemplate.getForObject(any(String.class), any())).thenReturn(response);
        String cityName = ipGeolocationService.getCityNameFromIP("100.42.20.12");
        assertEquals("Toronto", cityName);
    }

    @Test
    public void testGetCityNameFromIP_NullResponse() {
        when(restTemplate.getForObject(any(String.class), any())).thenReturn(null);
        String cityName = ipGeolocationService.getCityNameFromIP("123.456.789.10");
        assertNull(cityName);
    }
}
