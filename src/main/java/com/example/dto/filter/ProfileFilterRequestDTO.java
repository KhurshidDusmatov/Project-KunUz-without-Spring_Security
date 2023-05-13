package com.example.dto.filter;

import com.example.enums.ProfileRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileFilterRequestDTO {
    //    name,surname,phone,role,created_date_from,created_date_to
    @NotNull(message = "Name required")
    @Size(min = 3, max = 225, message = "Name must be between 3 and 225 characters")
    private String name;
    @NotNull(message = " required")
    @Size(min = 3, max = 225, message = "Surname must be between 3 and 225 characters")
    private String surname;
    @NotNull(message = "Email required")
    @Size(min = 15, max = 225, message = "Email must be between 15 and 225 characters")
    private String email;
    @NotNull(message = "Phone required")
    @Size(min = 9, max = 13, message = "Phone must be between 9 or 13 characters")
    private String phone;
    @NotNull(message = "Password required")
    @Size(min = 8, max = 225, message = "Password must be between 8 and 225 characters")
    private String password;
    @NotNull
    @NotEmpty
    private ProfileRole role;
    @NotNull
    @NotEmpty
    private LocalDateTime createdDateFrom;
    @NotNull
    @NotEmpty
    private LocalDateTime createdDateTo;
}
