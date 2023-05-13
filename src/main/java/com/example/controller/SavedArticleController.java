package com.example.controller;

import com.example.dto.article.ArticleRequestDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.dto.savedArticle.SavedArticleRequestDTO;
import com.example.dto.savedArticle.SavedArticleResponseDTO;
import com.example.dto.tag.TagDTO;
import com.example.dto.tag.TagRequestDTO;
import com.example.enums.ProfileRole;
import com.example.service.SavedArticleService;
import com.example.service.TagService;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/saved-article")
public class SavedArticleController {

    @Autowired
    private SavedArticleService savedArticleService;

    @PostMapping({"", "/"})
    public ResponseEntity<SavedArticleRequestDTO> create(@RequestBody SavedArticleRequestDTO dto,
                                         @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN,
                ProfileRole.MODERATOR, ProfileRole.USER,ProfileRole.PUBLISHER );
        return ResponseEntity.ok(savedArticleService.create(dto, jwtDTO.getId()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") String id,
                                          @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN,
                ProfileRole.MODERATOR, ProfileRole.USER,ProfileRole.PUBLISHER );
        return ResponseEntity.ok(savedArticleService.delete(id, jwtDTO.getId()));
    }

    @GetMapping(value = "/get-saved-articles")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN,
                ProfileRole.MODERATOR, ProfileRole.USER,ProfileRole.PUBLISHER );
        List<SavedArticleResponseDTO> list = savedArticleService.getAll(jwtDTO.getId());
        return ResponseEntity.ok(list);
    }
}
