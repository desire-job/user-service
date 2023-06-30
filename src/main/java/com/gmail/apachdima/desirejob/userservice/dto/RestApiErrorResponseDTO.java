package com.gmail.apachdima.desirejob.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RestApiErrorResponseDTO {

    private HttpStatus status;
    private String message;
    private List<String> errors;
    private LocalDateTime timestamp;

    public RestApiErrorResponseDTO(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        this.errors = List.of(error);
    }
}
