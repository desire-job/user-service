package com.gmail.apachdima.desirejob.userservice.service;

import com.gmail.apachdima.desirejob.commonservice.dto.user.UpdateUserRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserRequestDTO;
import com.gmail.apachdima.desirejob.commonservice.dto.user.UserResponseDTO;
import com.gmail.apachdima.desirejob.userservice.model.User;

import java.util.List;
import java.util.Locale;

public interface UserService {
    List<UserResponseDTO> findAll(/*Pageable pageable*/);
    UserResponseDTO findById(String userId, Locale locale);
    User getByUserName(String name, Locale locale);
    UserResponseDTO update(String userId, UpdateUserRequestDTO request, Locale locale);
    UserResponseDTO create(UserRequestDTO request);
    void delete(String userId, Locale locale);
}
