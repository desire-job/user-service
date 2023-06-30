package com.gmail.apachdima.desirejob.userservice.service.impl;

import com.gmail.apachdima.desirejob.userservice.service.UserService;
import com.gmail.apachdima.desirejob.userservice.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.toUSerDetails(userService.getUserByUserName(username, Locale.ENGLISH));
    }
}
