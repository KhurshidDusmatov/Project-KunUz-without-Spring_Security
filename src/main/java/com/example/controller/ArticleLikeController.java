package com.example.controller;

import com.example.dto.article.ArticleRequestDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.service.ArticleLikeService;
import com.example.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article-like")
public class ArticleLikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @GetMapping( "/like{articleId}")
    public ResponseEntity<?> like(@PathVariable("articleId") String articleId,
                                  @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(articleLikeService.like(articleId, jwtDTO.getId()));
    }
    @GetMapping( "/dislike{articleId}")
    public ResponseEntity<?> dislike(@PathVariable("articleId") String articleId,
                                  @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(articleLikeService.dislike(articleId, jwtDTO.getId()));
    }
    @GetMapping( "/delete{articleId}")
    public ResponseEntity<?> delete(@PathVariable("articleId") String articleId,
                                   @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(articleLikeService.delete(articleId, jwtDTO.getId()));
    }
}
