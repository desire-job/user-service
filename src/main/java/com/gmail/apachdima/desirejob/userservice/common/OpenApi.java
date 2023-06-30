package com.gmail.apachdima.desirejob.userservice.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OpenApi {

    OPEN_API_INFO_TITLE("User Service REST API documentation");

    private final String value;
}
