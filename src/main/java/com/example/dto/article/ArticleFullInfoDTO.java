package com.example.dto.article;

import com.example.dto.attach.AttachDTO;
import com.example.dto.category.CategoryDTO;
import com.example.dto.category.CategoryResponseDTO;
import com.example.dto.region.RegionDTO;
import com.example.dto.region.RegionResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFullInfoDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Integer sharedCount = 0;
    private RegionDTO region;
    private CategoryDTO category;
    private ArticleTypeDTO articleType;
    private LocalDateTime publishedDate;
    private Integer viewCount;
    private Integer likeCount;
    private AttachDTO image;
}
