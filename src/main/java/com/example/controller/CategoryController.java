package com.example.controller;

import com.example.dto.category.CategoryDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.service.CategoryService;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")

public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping({"", "/"})
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO dto,
                                              @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.create(dto, jwtDTO.getId()));
    }
    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam("id") Integer id,
                                         @RequestBody CategoryDTO dto,
                                         @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.update(id, dto, jwtDTO.getId()));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") Integer id,
                                          @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.delete(id, jwtDTO.getId()));
    }
    @GetMapping(value = "/get-all")
    public ResponseEntity<?> getAll() {
        List<CategoryDTO> list = categoryService.getAll();
        return ResponseEntity.ok(list);
    }

//    @GetMapping(value = "/get-by-language")
////    public ResponseEntity<?> getByLanguage(@RequestParam("language") String language) {
////        List<CategoryDTO> list = categoryService.getByLanguage(language);
////        return ResponseEntity.ok(list);
////    }


}
