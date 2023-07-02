package com.gmail.apachdima.desirejob.userservice.service.impl;

import com.gmail.apachdima.desirejob.commonservice.constant.Model;
import com.gmail.apachdima.desirejob.commonservice.constant.message.CommonError;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserResponseDTO;
import com.gmail.apachdima.desirejob.commonservice.exception.EntityNotFoundException;
import com.gmail.apachdima.desirejob.userservice.service.UserService;
import com.gmail.apachdima.desirejob.userservice.model.User;
import com.gmail.apachdima.desirejob.userservice.model.UserRole;
import com.gmail.apachdima.desirejob.userservice.repository.RoleRepository;
import com.gmail.apachdima.desirejob.userservice.repository.UserRepository;
import com.gmail.apachdima.desirejob.userservice.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
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
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserResponseDTO);
    }

    @Override
    public User getUserByUserName(String name, Locale locale) {
        return getByUserName(name, locale);
    }

    private User getByUserName(String userName, Locale locale) {
        Object[] params = new Object[]{Model.USER.getName(), Model.Field.USER_NAME.getFieldName(), userName};
        return userRepository
            .findByUserName(userName)
            .orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage(CommonError.ENTITY_NOT_FOUND.getKey(), params, locale)));
    }
}
