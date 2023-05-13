package com.example.controller;

import com.example.dto.tag.TagDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.dto.tag.TagRequestDTO;
import com.example.enums.ProfileRole;
import com.example.service.TagService;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping({"", "/"})
    public ResponseEntity<TagDTO> create(@RequestBody TagRequestDTO dto,
                                         @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN, ProfileRole.MODERATOR);
        return ResponseEntity.ok(tagService.create(dto, jwtDTO.getId()));
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam("id") Integer id,
                                         @RequestBody TagDTO dto,
                                         @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN, ProfileRole.MODERATOR);
        return ResponseEntity.ok(tagService.update(id, dto, jwtDTO.getId()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") Integer id,
                                          @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN, ProfileRole.MODERATOR);
        return ResponseEntity.ok(tagService.delete(id, jwtDTO.getId()));
    }

    @GetMapping(value = "/get-all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN, ProfileRole.MODERATOR);
        List<TagDTO> list = tagService.getAll();
        return ResponseEntity.ok(list);
    }
}
