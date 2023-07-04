package com.gmail.apachdima.desirejob.userservice.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Error {

    ERROR_TEST("error.user-service.test");

    private final String key;
}
