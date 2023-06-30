package com.gmail.apachdima.desirejob.userservice.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Entity {

    USER("User");

    private final String name;

    @AllArgsConstructor
    @Getter
    public enum Field {

        USER_ID("user id"),
        USER_NAME("user name");

        private final String fieldName;
    }
}
