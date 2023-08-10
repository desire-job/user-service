package com.gmail.apachdima.desirejob.userservice.service.impl;

import com.gmail.apachdima.desirejob.commonservice.constant.CommonConstant;
import com.gmail.apachdima.desirejob.commonservice.constant.message.CommonError;
import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInResponseDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserResponseDTO;
import com.gmail.apachdima.desirejob.commonservice.exception.AuthServerSignOutException;
import com.gmail.apachdima.desirejob.userservice.client.AuthServerClient;
import com.gmail.apachdima.desirejob.userservice.model.User;
import com.gmail.apachdima.desirejob.userservice.service.AuthService;
import com.gmail.apachdima.desirejob.userservice.service.UserService;
import com.gmail.apachdima.desirejob.userservice.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthServerClient authServerClient;
    private final UserService userService;
    private final MessageSource messageSource;
    private final UserMapper userMapper;

    @Override
    public SignInResponseDTO signIn(SignInRequestDTO request) {
        return authServerClient.signIn(request);
    }

    @Override
    public void signUp(UserRequestDTO request) {
        userService.create(request);
    }

    @Override
    public UserResponseDTO getCurrentUser(Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getByUserName(authentication.getName(), locale);
        return userMapper.toUserResponseDTO(currentUser);
    }

    @Override
    public void signOut(String refreshToken, HttpServletRequest request) {
        String bearer = request.getHeader(CommonConstant.AUTH_HEADER.getValue());
        String accessToken = StringUtils.replace(bearer, CommonConstant.BEARER_AUTH_HEADER_PREFIX.getValue(), StringUtils.EMPTY);
        HttpStatus status = authServerClient.signOut(accessToken, refreshToken);
        if (status.isError()) {
            throw  new AuthServerSignOutException(
                messageSource.getMessage(CommonError.AUTH_SERVER_SIGN_OUT.getKey(), null, Locale.ENGLISH));
        }
        HttpSession session = request.getSession(false);
        if (Objects.nonNull(session)) {
            session.invalidate();
        }
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();
    }
}
