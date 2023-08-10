package com.gmail.apachdima.desirejob.userservice.client;

import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInResponseDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserRequestDTO;
import org.springframework.http.HttpStatus;

public interface AuthServerClient {

    SignInResponseDTO signIn(SignInRequestDTO request);
    SignInResponseDTO adminSignIn();
    HttpStatus signOut(String accessToken, String refreshToken);
    HttpStatus createUser(UserRequestDTO request, String bearer);
}
