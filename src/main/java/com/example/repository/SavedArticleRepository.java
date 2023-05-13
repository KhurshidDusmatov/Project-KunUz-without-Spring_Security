package com.example.repository;

import com.example.entity.SavedArticleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedArticleRepository extends
        CrudRepository<SavedArticleEntity, Integer> {

    @Query("delete from SavedArticleEntity where articleId =:articleId and ownerId =:ownerId")
    int deleteSavedArticle(@Param("articleId") String articleId, @Param("ownerId") Integer ownerId);

    @Query("from SavedArticleEntity where ownerId =:ownerId")
    List<SavedArticleEntity> getAll(@Param("ownerId") Integer ownerId);
}
