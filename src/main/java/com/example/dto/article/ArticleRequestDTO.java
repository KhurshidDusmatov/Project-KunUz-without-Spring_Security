package com.example.dto.article;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ArticleRequestDTO {
    @NotNull(message = "title required")
    @Size(min = 3, max = 225, message = "Title must be between 3 and 225 characters")
    private String title;
    @NotNull(message = "description required")
    @Size(min = 5, max = 225, message = "description must be between 5 and 225 characters")
    private String description;
    @NotBlank(message = "Content qani")
    private String content;
    @NotNull(message = "item required")
    private String attachId;
    @NotNull(message = "item required")
    private Integer regionId;
    @NotNull(message = "item required")
    private Integer categoryId;
    @NotNull(message = " articleType required")
    private Integer articleTypeId;

}
