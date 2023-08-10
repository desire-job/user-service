package com.gmail.apachdima.desirejob.userservice.util.mapper;

import com.gmail.apachdima.desirejob.commonservice.dto.user.UserResponseDTO;
import com.gmail.apachdima.desirejob.userservice.model.Role;
import com.gmail.apachdima.desirejob.userservice.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    UserResponseDTO toUserResponseDTO(User user);

    default String roleToString(Role role) {
        return (Objects.isNull(role)) ? null : role.getName();
    }

    default LocalDateTime createdAtToLocalDateTime(long value) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), TimeZone.getDefault().toZoneId());
    }
}
