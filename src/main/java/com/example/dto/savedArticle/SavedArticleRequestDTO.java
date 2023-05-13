package com.example.dto.savedArticle;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavedArticleRequestDTO {
    @NotBlank
    String articleId;
}
