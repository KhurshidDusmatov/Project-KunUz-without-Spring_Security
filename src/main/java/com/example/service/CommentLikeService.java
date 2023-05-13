package com.example.service;

import com.example.entity.CommentLikeEntity;
import com.example.enums.EmotionStatus;
import com.example.repository.ArticleLikeRepository;
import com.example.repository.CommentLikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentLikeService {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public boolean  like(Integer commentId, Integer profileId){
        makeEmotion(commentId, profileId, EmotionStatus.LIKE);
        return true;
    }
    public boolean  dislike(Integer commentId, Integer profileId) {
        makeEmotion(commentId, profileId, EmotionStatus.DISLIKE);
        return true;
    }

    private void makeEmotion(Integer commentId, Integer profileId, EmotionStatus status){
        Optional<CommentLikeEntity> optional = commentLikeRepository.findByCommentIdAndProfileId(commentId, profileId);
        if (optional.isEmpty()) {
            CommentLikeEntity entity = new CommentLikeEntity();
            entity.setCommentId(commentId);
            entity.setProfileId(profileId);
            entity.setStatus(status);
            commentLikeRepository.save(entity);
        }else {
            commentLikeRepository.update(status, commentId, profileId);
        }
    }

    public boolean delete(Integer commentId,Integer profileId){
        commentLikeRepository.delete(commentId, profileId);
        return true;
    }
}
