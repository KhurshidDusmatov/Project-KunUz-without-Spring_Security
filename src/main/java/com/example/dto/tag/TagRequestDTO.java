package com.example.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagRequestDTO {
    @NotBlank
    private String name;
}
