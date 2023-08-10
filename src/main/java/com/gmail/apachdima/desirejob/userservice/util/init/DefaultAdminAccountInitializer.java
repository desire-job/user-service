package com.gmail.apachdima.desirejob.userservice.util.init;

import com.gmail.apachdima.desirejob.commonservice.constant.message.CommonError;
import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInResponseDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.exception.EntityCreationException;
import com.gmail.apachdima.desirejob.userservice.client.AuthServerClient;
import com.gmail.apachdima.desirejob.userservice.model.Role;
import com.gmail.apachdima.desirejob.userservice.model.Roles;
import com.gmail.apachdima.desirejob.userservice.model.User;
import com.gmail.apachdima.desirejob.userservice.repository.RoleRepository;
import com.gmail.apachdima.desirejob.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class DefaultAdminAccountInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthServerClient authServerClient;
    private final MessageSource messageSource;

    @Value("${app.auth-server.default-user.username}")
    private String username;
    @Value("${app.auth-server.default-user.password}")
    private String password;
    @Value("${app.auth-server.default-user.email}")
    private String email;

    @Override
    public void run(String... args) {
        Optional<User> optionalAdmin = userRepository.findByUsername(username);
        if (optionalAdmin.isEmpty()) {
            SignInResponseDTO adminSignInResponse = authServerClient.adminSignIn();
            UserRequestDTO request = UserRequestDTO.builder()
                .email(email)
                .password(password)
                .role(Roles.ADMIN.name())
                .build();
            HttpStatus status = authServerClient.createUser(request, adminSignInResponse.getAccessToken());
            if (status.isError()) {
                throw new EntityCreationException(
                    messageSource
                        .getMessage(CommonError.ENTITY_CREATION_EXCEPTION.getKey(), new Object[]{username}, Locale.ENGLISH));
            }
            Optional<User> savedUserOptional = userRepository.findByUsername(username);
            if (savedUserOptional.isPresent()) {
                Role role = roleRepository.findByName(Roles.ADMIN.name()).get();
                savedUserOptional.get().setRoles(Set.of(role));
                userRepository.save(savedUserOptional.get());
            }
        }
    }
}
