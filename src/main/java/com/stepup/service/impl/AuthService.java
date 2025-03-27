package com.stepup.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
public class AuthService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String googleUserInfoUri;

    public String generateAuthUrl() {
        String url = "";
        GoogleAuthorizationCodeRequestUrl urlBuilder = new GoogleAuthorizationCodeRequestUrl(
                googleClientId,
                googleRedirectUri,
                Arrays.asList("email", "profile", "openid"));
        url = urlBuilder.build();

        return url;
    }

    public Map<String, Object> authenticateAndFetchProfile(String code) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        String accessToken;

        accessToken = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(), new GsonFactory(),
                googleClientId,
                googleClientSecret,
                code,
                googleRedirectUri
        ).execute().getAccessToken();

        // Configure RestTemplate to include the access token in the Authorization header
        restTemplate.getInterceptors().add((req, body, executionContext) -> {
            req.getHeaders().set("Authorization", "Bearer " + accessToken);
            return executionContext.execute(req, body);
        });

        // Make a GET request to fetch user information
        return new ObjectMapper().readValue(
                restTemplate.getForEntity(googleUserInfoUri, String.class).getBody(),
                new TypeReference<>() {
                });
    }
}
