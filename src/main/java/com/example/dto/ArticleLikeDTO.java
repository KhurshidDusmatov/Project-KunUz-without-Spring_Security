package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleLikeDTO {
    @NotNull(message = "Article can not be null")
    private String articleId;

}
