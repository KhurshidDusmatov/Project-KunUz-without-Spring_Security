package com.example.repository;

import com.example.dto.article.ArticleFilterDTO;
import com.example.dto.comment.CommentFilterDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.CommentEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentCustomRepository {
    @Autowired
    private EntityManager entityManager;
    public PageImpl<CommentEntity> filter(CommentFilterDTO filterDTO, int page, int size){
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (filterDTO.getId() != null) {
            builder.append(" and s.title id =:id");
            params.put("id", filterDTO.getId());
        }
        if (filterDTO.getOwnerId() != null) {
            builder.append(" and s.ownerId =:ownerId");
            params.put("ownerId", filterDTO.getOwnerId());
        }
        if (filterDTO.getArticleId() != null) {
            builder.append(" and s.articleId like :articleId");
            params.put("articleId", filterDTO.getArticleId());
        }
        if (filterDTO.getCreatedDateFrom() != null && filterDTO.getCreatedDateTo() != null) {
            builder.append(" and s.createdDate between :dateFrom and dateTo ");
            params.put("dateFrom", LocalDateTime.of(filterDTO.getCreatedDateFrom(), LocalTime.MIN));
            params.put("dateTo", LocalDateTime.of(filterDTO.getCreatedDateTo(), LocalTime.MIN));
        } else if (filterDTO.getCreatedDateFrom() != null) {
            builder.append(" and s.createdDate >= :dateFrom ");
            params.put("dateFrom", LocalDateTime.of(filterDTO.getCreatedDateFrom(), LocalTime.MIN));
        } else if (filterDTO.getCreatedDateTo() != null) {
            builder.append(" and s.createdDate <= :dateTo ");
            params.put("dateTo", LocalDateTime.of(filterDTO.getCreatedDateTo(), LocalTime.MIN));
        }

        StringBuilder selectBuilder = new StringBuilder("From CommentEntity as s where visible = true ");
        selectBuilder.append(builder);

        StringBuilder countBuilder = new StringBuilder("select count(s) from CommentEntity as s where visible = true ");
        countBuilder.append(builder);

        Query selectQuery = this.entityManager.createQuery(selectBuilder.toString());
        Query countQuery = this.entityManager.createQuery(countBuilder.toString());

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }

        selectQuery.setFirstResult((page - 1) * size); // offset
        selectQuery.setMaxResults(size);
        List<CommentEntity> commentEntityList = selectQuery.getResultList();
        long totalCount = (long) countQuery.getSingleResult();


        return new PageImpl<>(commentEntityList, PageRequest.of(page, size), totalCount);
    }

}
