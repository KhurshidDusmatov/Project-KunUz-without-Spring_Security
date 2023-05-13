package com.example.service;

import com.example.dto.article.ArticleShortInfoDTO;
import com.example.dto.savedArticle.SavedArticleRequestDTO;
import com.example.dto.savedArticle.SavedArticleResponseDTO;
import com.example.entity.SavedArticleEntity;
import com.example.repository.SavedArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SavedArticleService {
    @Autowired
    private SavedArticleRepository savedArticleRepository;
    private final ArticleService articleService;
    public SavedArticleRequestDTO create(SavedArticleRequestDTO dto, Integer ownerId) {
        SavedArticleEntity entity = new SavedArticleEntity();
        entity.setArticleId(dto.getArticleId());
        entity.setOwnerId(ownerId);
        savedArticleRepository.save(entity);
        return dto;
    }
    public Boolean delete(String articleId, Integer ownerId) {
        savedArticleRepository.deleteSavedArticle(articleId, ownerId);
        return true;
    }

    public List<SavedArticleResponseDTO> getAll(Integer ownerId) {
        List<SavedArticleEntity> all = savedArticleRepository.getAll(ownerId);
        List<SavedArticleResponseDTO> dtos = new ArrayList<>();
        all.forEach(savedArticleEntity -> {
            dtos.add(toDTO(savedArticleEntity));
        });
        return dtos;
    }

    private SavedArticleResponseDTO toDTO(SavedArticleEntity entity){
        SavedArticleResponseDTO dto = new SavedArticleResponseDTO();
        ArticleShortInfoDTO info = articleService.toArticleShortInfo(entity.getArticle());
        dto.setId(entity.getId());
        dto.setArticleShortInfoDTO(info);
        return dto;
    }
}
