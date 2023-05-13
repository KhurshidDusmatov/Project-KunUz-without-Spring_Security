package com.example.dto.comment;

import com.example.dto.article.ArticleShortInfoDTO;
import com.example.dto.profile.ProfileShortInfoDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDTO {
        private Integer id;
        private LocalDateTime createdDate;
        private LocalDateTime updateDate;
        private ProfileShortInfoDTO profile;
        private String content;
        private ArticleShortInfoDTO article;
        private Integer replyId;
}

