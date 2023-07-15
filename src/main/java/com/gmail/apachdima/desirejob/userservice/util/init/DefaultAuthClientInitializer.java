package com.gmail.apachdima.desirejob.userservice.util.init;

import com.gmail.apachdima.desirejob.userservice.model.AuthClient;
import com.gmail.apachdima.desirejob.userservice.repository.AuthClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Profile("!test")
@Component
@Transactional
@RequiredArgsConstructor
public class DefaultAuthClientInitializer implements CommandLineRunner {

    private final AuthClientRepository authClientRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-client.client-id}")
    private String clientId;
    @Value("${app.default-client.client-secret}")
    private String clientSecret;

    @Override
    public void run(String... args) {
        Optional<AuthClient> optionalDefaultAuthClient = authClientRepository.findByClientId(clientId);
        if (optionalDefaultAuthClient.isEmpty()) {
            AuthClient authClient = AuthClient.builder()
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(clientSecret))
                .scope("read,write")
                .grantTypes("client_credentials,password,refresh_token")
                .accessTokenValidity(600)
                .refreshTokenValidity(1200)
                .build();
            authClientRepository.save(authClient);
        }
    }
}
