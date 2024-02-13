package com.Registration.UserRegister.service;

import com.Registration.UserRegister.model.IPGeolocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class IPGeolocationServiceImpl implements IPGeolocationService {

    private static final String IP_API_URL = "http://ip-api.com/json/";

    @Autowired
    private RestTemplate restTemplate;
    private final Map<String, IPGeolocationResponse> cache;

    public IPGeolocationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.cache = new HashMap<>();
    }

    @Override
    public boolean isCanadianIP(String ip) {
        IPGeolocationResponse response = getResponse(ip);
        return response != null && "CA".equals(response.getCountryCode());
    }

    @Override
    public String getCityNameFromIP(String ip) {
        IPGeolocationResponse response = getResponse(ip);
        return response != null ? response.getCity() : null;
    }

    private IPGeolocationResponse getResponse(String ip) {
        if (cache.containsKey(ip)) {
            return cache.get(ip);
        } else {
            String apiUrl = IP_API_URL + ip;
            IPGeolocationResponse response = restTemplate.getForObject(apiUrl, IPGeolocationResponse.class);
            cache.put(ip, response);
            return response;
        }
    }
}
