package com.example.repository;

import com.example.entity.CommentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Integer>,
        PagingAndSortingRepository<CommentEntity, Integer> {
    Optional<CommentEntity> findByIdAndOwnerId(Integer commentId, Integer ownerId);

    @Transactional
    @Modifying
    @Query("update CommentEntity  set visible = false where id =:id")
    int updateVisible(@Param("id") Integer id);
    List<CommentEntity> findAllByArticleId(String articleId);

    @Query("from CommentEntity where replyId =:replyId")
    List<CommentEntity> getByReplyId(@Param("replyId") Integer id);
}
