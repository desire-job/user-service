package com.gmail.apachdima.desirejob.userservice.util.init;

import com.gmail.apachdima.desirejob.userservice.model.User;
import com.gmail.apachdima.desirejob.userservice.model.UserRole;
import com.gmail.apachdima.desirejob.userservice.repository.RoleRepository;
import com.gmail.apachdima.desirejob.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Profile("!test")
@Component
@Transactional
@RequiredArgsConstructor
public class DefaultAdminAccountInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default.admin.username}")
    private String username;
    @Value("${app.default.admin.password}")
    private String password;
    @Value("${app.default.admin.email}")
    private String email;

    @Override
    public void run(String... args) {
        Optional<User> optionalDefaultAdmin = userRepository.findByUserName(username);
        if (optionalDefaultAdmin.isEmpty()) {
            User defaultAdmin = User.builder()
                .userName(username)
                .password(passwordEncoder.encode(password))
                .firstName("super-admin")
                .lastName("super-admin")
                .email(email)
                .enabled(true)
                .created(LocalDateTime.now())
                .roles(new HashSet<>(roleRepository.findAll()))
                .build();
            userRepository.save(defaultAdmin);
        }
    }
}
