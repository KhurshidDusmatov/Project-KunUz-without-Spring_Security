package com.example.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthDTO {
    @NotNull(message = "Email required")
    @Size(min = 15, max = 225, message = "Email must be between 15 and 225 characters")
    private String email;

    @NotNull(message = "Password required")
    @Size(min = 1, max = 225, message = "Email must be between 1 and 225 characters")
    private String password;
}
