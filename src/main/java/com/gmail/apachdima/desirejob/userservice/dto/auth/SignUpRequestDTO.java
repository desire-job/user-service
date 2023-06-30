package com.gmail.apachdima.desirejob.userservice.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUpRequestDTO {

    @NotBlank
    @Size(min = 2, max = 50)
    private String userName;
    @Size(min = 2, max = 50)
    private String password;
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;
    @NotBlank
    @Email
    private String email;
}
