package com.gmail.apachdima.desirejob.userservice.service.impl;

import com.gmail.apachdima.desirejob.userservice.exception.EntityNotFoundException;
import com.gmail.apachdima.desirejob.userservice.exception.UserServiceApplicationException;
import com.gmail.apachdima.desirejob.userservice.service.UserService;
import com.gmail.apachdima.desirejob.userservice.common.Entity;
import com.gmail.apachdima.desirejob.userservice.common.message.Error;
import com.gmail.apachdima.desirejob.userservice.dto.user.UserRequestDTO;
import com.gmail.apachdima.desirejob.userservice.dto.user.UserResponseDTO;
import com.gmail.apachdima.desirejob.userservice.dto.user.UserSearchRequestDTO;
import com.gmail.apachdima.desirejob.userservice.model.User;
import com.gmail.apachdima.desirejob.userservice.model.UserRole;
import com.gmail.apachdima.desirejob.userservice.repository.RoleRepository;
import com.gmail.apachdima.desirejob.userservice.repository.UserRepository;
import com.gmail.apachdima.desirejob.userservice.util.mapper.UserMapper;
import com.gmail.apachdima.desirejob.userservice.util.search.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        User user = User.builder()
            .userName(userRequestDTO.getUserName())
            .password(passwordEncoder.encode(userRequestDTO.getPassword().trim()))
            .firstName(userRequestDTO.getFirstName().trim())
            .lastName(userRequestDTO.getLastName().trim())
            .email(userRequestDTO.getEmail().trim())
            .enabled(true)
            .created(LocalDateTime.now())
            .roles(Set.of(roleRepository.findByRole(UserRole.USER).get()))
            .build();
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    @Override
    public Page<UserResponseDTO> search(Pageable pageable, UserSearchRequestDTO userSearchRequestDTO, Locale locale) {
        SpecificationBuilder builder = new SpecificationBuilder();
        if (Objects.nonNull(userSearchRequestDTO.getSearchCriteria())) {
            userSearchRequestDTO.getSearchCriteria().forEach(builder::with);
        }
        Page<User> userData;
        try {
            userData = userRepository.findAll(builder.build(), pageable);
        } catch (UnsupportedOperationException e) {
            throw new UserServiceApplicationException(
                messageSource.getMessage(
                    Error.SEARCH_CRITERIA_OPERATION_NOT_SUPPORTED.getKey(), new Object[]{e.getMessage()}, locale));
        }
        return userData.map(userMapper::toUserResponseDTO);
    }

    @Override
    public User getUserByUserName(String name, Locale locale) {
        return getByUserName(name, locale);
    }

    private User getByUserName(String userName, Locale locale) {
        Object[] params = new Object[]{Entity.USER.getName(), Entity.Field.USER_NAME.getFieldName(), userName};
        return userRepository
            .findByUserName(userName)
            .orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage(Error.ENTITY_NOT_FOUND.getKey(), params, locale)));
    }
}
