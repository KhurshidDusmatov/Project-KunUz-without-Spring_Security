package com.example.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class CommentFilterDTO {
    private Integer id;
    private LocalDate createdDateFrom;
    private LocalDate createdDateTo;
    private Integer ownerId;
    private String articleId;
}
