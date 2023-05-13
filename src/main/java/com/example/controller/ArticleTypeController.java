package com.example.controller;

import com.example.dto.article.ArticleTypeDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.service.ArticleTypeService;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article-type")

public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping({"", "/"})
    public ResponseEntity<ArticleTypeDTO> create(@RequestBody ArticleTypeDTO dto,
                                                 @RequestHeader("Authorization") String authorization) {

        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.create(dto, jwtDTO.getId()));
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam("id") Integer id,
                                             @RequestBody ArticleTypeDTO dto,
                                             @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.update(id, dto, jwtDTO.getId()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") Integer id,
                                         @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.delete(id, jwtDTO.getId()));
    }

    @GetMapping(value = "/pagination")
    public ResponseEntity<?> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "4") int size) {
        Page<ArticleTypeDTO> pagination = articleTypeService.pagination(page, size);
        return ResponseEntity.ok(pagination);
    }

    @GetMapping(value = "/get-by-language")
    public ResponseEntity<?> getByLanguage(@RequestParam("language") String language) {
        List<ArticleTypeDTO> list = articleTypeService.getByLanguage(language);
        return ResponseEntity.ok(list);
    }


}
