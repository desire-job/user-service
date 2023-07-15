package com.gmail.apachdima.desirejob.userservice.service.impl;

import com.gmail.apachdima.desirejob.commonservice.constant.CommonConstant;
import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignUpRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserResponseDTO;
import com.gmail.apachdima.desirejob.userservice.service.AuthService;
import com.gmail.apachdima.desirejob.userservice.service.UserService;
import com.gmail.apachdima.desirejob.userservice.model.User;
import com.gmail.apachdima.desirejob.userservice.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenStore tokenStore;
    private final RestTemplate restTemplate;
    private final UserMapper userMapper;

    @Override
    public void signIn(SignInRequestDTO request) {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void signUp(SignUpRequestDTO signUpRequestDTO) {
        userService.create(userMapper.toUserRequestDTO(signUpRequestDTO));
    }

    @Override
    public UserResponseDTO getCurrentUser(Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByUserName(authentication.getName(), locale);
        return userMapper.toUserResponseDTO(currentUser);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(CommonConstant.AUTH_HEADER.getValue());
        if (Objects.nonNull(authHeader) && authHeader.contains(CommonConstant.BEARER_AUTH_HEADER_PREFIX.getValue())) {
            String tokenValue = authHeader
                .replace(CommonConstant.BEARER_AUTH_HEADER_PREFIX.getValue(), StringUtils.EMPTY).trim();
            DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken)tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);

            HttpSession session = request.getSession(false);
            if (Objects.nonNull(session)) {
                session.invalidate();
            }
            new SecurityContextLogoutHandler().logout(request, null, null);
            try {
                //sending back to client app
                response.sendRedirect(request.getHeader("referer"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*String authHeader = request.getHeader(CommonConstant.AUTH_HEADER.getValue());
        String tokenValue = authHeader
            .replace(CommonConstant.BEARER_AUTH_HEADER_PREFIX.getValue(), StringUtils.EMPTY).trim();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("10101010", "11110000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", tokenValue);
        params.add("token_type_hint", "access_token");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<?> refreshTokenResponse = restTemplate
            .postForEntity("https://localhost:8084/user-service/oauth2/revoke", httpEntity, Object.class);
        refreshTokenResponse.getBody();*/

    }
}
