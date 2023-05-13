package com.example.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    @NotNull(message = "Name required")
    @Size(min = 3, max = 225, message = "Name must be between 3 and 225 characters")
    private String name;
    @NotNull(message = "Surname required")
    @Size(min = 3, max = 225, message = "Surname must be between 3 and 225 characters")
    private String surname;
    @NotNull(message = "Email required")
    @Size(min = 15, max = 225, message = " must be between 15 and 225 characters")
    private String email;
    @NotNull(message = "Phone required")
    @Size(min = 9, max = 13, message = " must be between 3 and 225 characters")
    private String phone;
    @NotNull(message = "Password required")
    @Size(min = 8, max = 225, message = " must be between 3 and 225 characters")
    private String password;
}
