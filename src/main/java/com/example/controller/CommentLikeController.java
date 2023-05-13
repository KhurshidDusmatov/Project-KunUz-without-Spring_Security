package com.example.controller;

import com.example.dto.jwt.JwtDTO;
import com.example.service.ArticleLikeService;
import com.example.service.CommentLikeService;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment-like")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;

    @GetMapping( "/like{articleId}")
    public ResponseEntity<?> like(@PathVariable("articleId") Integer commentId,
                                  @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(commentLikeService.like(commentId, jwtDTO.getId()));
    }
    @GetMapping( "/dislike{commentId}")
    public ResponseEntity<?> dislike(@PathVariable("commentId") Integer commentId,
                                  @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(commentLikeService.dislike(commentId, jwtDTO.getId()));
    }
    @GetMapping( "/delete{commentId}")
    public ResponseEntity<?> delete(@PathVariable("commentId") Integer commentId,
                                   @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(commentLikeService.delete(commentId, jwtDTO.getId()));
    }
}
