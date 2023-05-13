package com.example.dto.profile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateDTO {
    @NotNull(message = "Name required")
    @Size(min = 3, max = 225, message = "Name must be between 3 and 225 characters")
    private String name;
    @NotNull(message = "Surname required")
    @Size(min = 3, max = 225, message = "Surname must be between 3 and 225 characters")
    private String surname;
    @NotNull(message = "Name required")
    @Size(min = 15, max = 225, message = "Name must be between 3 and 225 characters")
    private String email;
    @NotNull(message = "Phone required")
    @Size(min = 9, max = 13, message = "Name must be between 9 and 13 characters")
    private String phone;
    @NotNull(message = "Password required")
    @Size(min = 8, max = 225, message = "Password must be between 8 and 225 characters")
    private String password;
}
