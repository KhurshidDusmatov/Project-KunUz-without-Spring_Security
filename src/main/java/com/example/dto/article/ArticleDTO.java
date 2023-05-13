package com.example.dto.article;

import com.example.enums.ArticleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDTO {
    @NotBlank
    private String id;
    @NotNull(message = "title required")
    @Size(min = 3, max = 225, message = "Title must be between 3 and 225 characters")
    private String title;
    @NotNull(message = "description required")
    @Size(min = 3, max = 225, message = "description must be between 3 and 225 characters")
    private String description;
    @NotBlank(message = "Content qani")
    private String content;
    @NotNull(message = "item required")
    @Positive
    private Integer sharedCount;
    @NotNull(message = "item required")
    @Positive
    private Integer imageId;
    @NotNull(message = "item required")
    @Positive
    private Integer regionId;
    @NotNull(message = "item required")
    @Positive
    private Integer categoryId;
    @NotNull(message = "item required")
    @Positive
    private Integer moderatorId;
    @NotNull(message = "item required")
    @Positive
    private Integer publisherId;
    @NotNull(message = "item required")
    @Positive
    private Integer viewCount;
    @NotNull(message = "item required")
    @Positive
    private String attachId;
    @NotNull(message = "item required")
    @Positive
    private ArticleStatus articleStatus;

}
