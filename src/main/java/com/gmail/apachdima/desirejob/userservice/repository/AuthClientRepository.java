package com.gmail.apachdima.desirejob.userservice.repository;

import com.gmail.apachdima.desirejob.userservice.model.AuthClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthClientRepository extends JpaRepository<AuthClient, String> {

    Optional<AuthClient> findByClientId(String clientId);
}
