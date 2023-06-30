package com.gmail.apachdima.desirejob.userservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequestDTO {

    @NotBlank
    @Size(min = 2, max = 255)
    private String userName;
    @NotBlank
    @Size(min = 4, max = 255)
    private String password;
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;
    @NotBlank
    @Size(max = 255)
    @Email
    private String email;

}
