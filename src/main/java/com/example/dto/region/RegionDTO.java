package com.example.dto.region;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDTO {
    @NotNull
    @Positive
    private Integer id;
    @NotNull(message = "Item required")
    @Size(min = 2, max = 225, message = "Item must be between 2 and 225 characters")
    private String nameUz;
    @NotNull(message = "Item required")
    @Size(min = 2, max = 225, message = "Item must be between 2 and 225 characters")
    private String nameRu;
    @NotNull(message = "Item required")
    @Size(min = 2, max = 225, message = "Item must be between 2 and 225 characters")
    private String nameEn;
    private Boolean visible;
    private LocalDateTime createdDate;
    private String name;
}
