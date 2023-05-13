package com.example.dto.savedArticle;

import com.example.dto.article.ArticleFullInfoDTO;
import com.example.dto.article.ArticleShortInfoDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavedArticleResponseDTO {
    private Integer id;
    private ArticleShortInfoDTO articleShortInfoDTO;
}
