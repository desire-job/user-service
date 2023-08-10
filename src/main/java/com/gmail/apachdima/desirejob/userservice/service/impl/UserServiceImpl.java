package com.gmail.apachdima.desirejob.userservice.service.impl;

import com.gmail.apachdima.desirejob.commonservice.constant.Model;
import com.gmail.apachdima.desirejob.commonservice.constant.message.CommonError;
import com.gmail.apachdima.desirejob.commonservice.dto.auth.SignInResponseDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UpdateUserRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserResponseDTO;
import com.gmail.apachdima.desirejob.commonservice.exception.EntityCreationException;
import com.gmail.apachdima.desirejob.commonservice.exception.EntityNotFoundException;
import com.gmail.apachdima.desirejob.userservice.client.AuthServerClient;
import com.gmail.apachdima.desirejob.userservice.model.Role;
import com.gmail.apachdima.desirejob.userservice.model.Roles;
import com.gmail.apachdima.desirejob.userservice.model.User;
import com.gmail.apachdima.desirejob.userservice.repository.RoleRepository;
import com.gmail.apachdima.desirejob.userservice.repository.UserRepository;
import com.gmail.apachdima.desirejob.userservice.service.UserService;
import com.gmail.apachdima.desirejob.userservice.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthServerClient authServerClient;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;
    private final UserMapper userMapper;

    @Value("${app.auth-server.realm.app-realm-id}")
    private String appRealmId;

    @Override
    public List<UserResponseDTO> findAll(/*Pageable pageable*/) {
        return userRepository.findByRealmId(appRealmId/*pageable*/).stream()
            .map(userMapper::toUserResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO findById(String userId, Locale locale) {
        return userMapper.toUserResponseDTO(getById(userId, locale));
    }

    @Override
    public User getByUserName(String name, Locale locale) {
        return getUserByUserName(name, locale);
    }

    @Override
    public UserResponseDTO update(String userId, UpdateUserRequestDTO request, Locale locale) {
        User user = getById(userId, locale);
        if (StringUtils.isNoneBlank(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }
        if (StringUtils.isNoneBlank(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO create(UserRequestDTO request) {
        SignInResponseDTO adminAccessResponse = authServerClient.adminSignIn();
        HttpStatus status = authServerClient.createUser(request, adminAccessResponse.getAccessToken());
        if (status.isError()) {
            throw new EntityCreationException(
                messageSource
                    .getMessage(CommonError.ENTITY_CREATION_EXCEPTION.getKey(), new Object[]{request.getEmail()}, Locale.ENGLISH));
        }
        User user = getUserByUserName(request.getEmail(), Locale.ENGLISH);
        Roles neededRoles = defineNeededRoles(request.getRole());
        Role role = getRoleByName(neededRoles);
        user.setRoles(Set.of(role));
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    @Override
    public void delete(String userId, Locale locale) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User loggedInUser = getUserByUserName(authentication.getName(), locale);
        User deletableUser = getById(userId, locale);
        Set<String> loggedInUserRoles = loggedInUser.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());
        Set<String> deletableUserRoles = deletableUser.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());
        if (loggedInUserRoles.contains(Roles.ADMIN.name())
            || (loggedInUserRoles.contains(Roles.MANAGER.name()) && deletableUserRoles.contains(Roles.USER.name()))) {
            userRepository.deleteById(userId);
        }
    }

    private Roles defineNeededRoles(String role) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Set<String> authorities = authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority())
            .collect(Collectors.toSet());
        if (authorities.contains("ROLE_ANONYMOUS") || Objects.isNull(role)) {
            return Roles.USER;
        }
        if (authorities.contains("ROLE_" + Roles.ADMIN.name())) {
            if (Roles.getAdminGrantedRoles().contains(role)) {
                return Roles.valueOf(role);
            }
        }
        if (authorities.contains("ROLE_" + Roles.MANAGER.name())) {
            return Roles.USER;
        }
        throw new EntityNotFoundException(messageSource.getMessage(
            CommonError.ENTITY_NOT_FOUND.getKey(),
            new Object[]{Model.ROLE.getName(), Model.Field.NAME.getFieldName(), role},
            Locale.ENGLISH));
    }

    private User getUserByUserName(String username, Locale locale) {
        return userRepository
            .findByUsername(username)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    messageSource.getMessage(
                        CommonError.ENTITY_NOT_FOUND.getKey(),
                        new Object[]{Model.USER.getName(), Model.Field.USER_NAME.getFieldName(), username},
                        Objects.nonNull(locale) ? locale : Locale.ENGLISH)));
    }

    private Role getRoleByName(Roles neededRoles) {
        return roleRepository
            .findByName(neededRoles.name())
            .orElseThrow(() ->
                new EntityNotFoundException(
                    messageSource.getMessage(
                        CommonError.ENTITY_NOT_FOUND.getKey(),
                        new Object[]{Model.ROLE.getName(), Model.Field.NAME.getFieldName(), Roles.USER.name()},
                        Locale.ENGLISH)));
    }

    private User getById(String userId, Locale locale) {
        return userRepository
            .findById(userId)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    messageSource.getMessage(
                        CommonError.ENTITY_NOT_FOUND.getKey(),
                        new Object[]{Model.USER.getName(), Model.Field.ID.getFieldName(),userId},
                        locale)));
    }
}
