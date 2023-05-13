package com.example.dto.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.metamodel.mapping.internal.InFlightEntityMappingType;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
}
