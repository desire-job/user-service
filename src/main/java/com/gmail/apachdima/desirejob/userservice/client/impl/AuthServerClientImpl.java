package com.gmail.apachdima.desirejob.userservice.client.impl;

import com.gmail.apachdima.desirejob.commonservice.constant.CommonConstant;
import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInResponseDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserRequestDTO;
import com.gmail.apachdima.desirejob.userservice.client.AuthServerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthServerClientImpl implements AuthServerClient {

    private static final String OPENID_CONNECT_URI = "/protocol/openid-connect";
    private static final String TOKEN = "/token";
    private static final String REALMS_URI = "/realms/";
    @Value("${app.auth-server.admin.username}")
    private String adminUsername;
    @Value("${app.auth-server.admin.password}")
    private String adminPassword;
    @Value("${app.auth-server.base-url}")
    private String baseUrl;
    @Value("${app.auth-server.realm.admin-realm}")
    private String adminRealm;
    @Value("${app.auth-server.realm.app-realm}")
    private String appRealm;
    @Value("${app.auth-server.client.client-id}")
    private String clientId;
    @Value("${app.auth-server.client.client-secret}")
    private String clientSecret;

    @Override
    public SignInResponseDTO signIn(SignInRequestDTO request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", request.getUserName());
        params.add("password", request.getPassword());
        params.add("client_id", request.getClientId());
        params.add("client_secret", request.getClientSecret());
        params.add("grant_type", request.getGrantType());
        params.add("scope", request.getScope());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<SignInResponseDTO> response = new RestTemplate()
            .postForEntity(baseUrl + REALMS_URI + appRealm + OPENID_CONNECT_URI + TOKEN, httpEntity, SignInResponseDTO.class);
        return response.getBody();
    }

    @Override
    public SignInResponseDTO adminSignIn() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
        payload.add("username", adminUsername);
        payload.add("password", adminPassword);
        payload.add("grant_type", "password");
        payload.add("client_id", "admin-cli");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<SignInResponseDTO> adminAccessResponse = new RestTemplate()
            .postForEntity(baseUrl + REALMS_URI + adminRealm + OPENID_CONNECT_URI + TOKEN, httpEntity, SignInResponseDTO.class);
        return adminAccessResponse.getBody();
    }

    @Override
    public HttpStatus signOut(String accessToken, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(CommonConstant.AUTH_HEADER.getValue(),
            CommonConstant.BEARER_AUTH_HEADER_PREFIX.getValue().concat(accessToken));

        MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
        payload.add("refresh_token", refreshToken);
        payload.add("client_id", clientId);
        payload.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<?> adminAccessResponse = new RestTemplate()
            .postForEntity(baseUrl + REALMS_URI + appRealm + OPENID_CONNECT_URI + "/logout", httpEntity, Object.class);

        payload.remove("refresh_token");
        payload.add("token", refreshToken);
        payload.add("token_type_hint", "refresh_token");

        httpEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<?> revokeAccessTokenResponse = new RestTemplate()
            .postForEntity(baseUrl + REALMS_URI + appRealm + OPENID_CONNECT_URI + "/revoke", httpEntity, Object.class);

        return adminAccessResponse.getStatusCode().is2xxSuccessful()
            && revokeAccessTokenResponse.getStatusCode().is2xxSuccessful()
            ? HttpStatus.OK
            : HttpStatus.BAD_REQUEST;
    }

    @Override
    public HttpStatus createUser(UserRequestDTO request, String bearer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(CommonConstant.AUTH_HEADER.getValue(),
            CommonConstant.BEARER_AUTH_HEADER_PREFIX.getValue().concat(bearer));

        JSONObject body = new JSONObject();
        JSONObject credentials = new JSONObject();
        try {
            body.put("username", request.getEmail());
            body.put("email", request.getEmail());
            body.put("firstName", request.getFirstName());
            body.put("lastName", request.getLastName());
            credentials.put("type", "password");
            credentials.put("value", request.getPassword());
            credentials.put("temporary", false);
            body.put("credentials", new JSONArray(List.of(credentials)));
            body.put("enabled", true);
            body.put("emailVerified", false);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HttpEntity<String> creationRequest = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<?> response = new RestTemplate()
            .postForEntity(baseUrl + "/admin" + REALMS_URI + appRealm + "/users", creationRequest, Object.class);
        return response.getStatusCode();
    }
}
