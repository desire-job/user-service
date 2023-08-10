package com.gmail.apachdima.desirejob.userservice.service;

import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInResponseDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserResponseDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public interface AuthService {

    SignInResponseDTO signIn(SignInRequestDTO signInRequestDTO);
    void signUp(UserRequestDTO request);
    UserResponseDTO getCurrentUser(Locale locale);
    void signOut(String refreshToken, HttpServletRequest request);
}
