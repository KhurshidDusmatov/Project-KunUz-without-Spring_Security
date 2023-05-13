package com.example.controller;

import com.example.dto.filter.ProfileFilterRequestDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.dto.profile.ProfileDTO;
import com.example.dto.profile.ProfileUpdateDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping({"", "/"})
    public ResponseEntity<ProfileDTO> create(@RequestBody ProfileDTO dto,
                                             @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.create(dto, jwtDTO.getId()));
    }

    @PutMapping("/update-by-admin")
    public ResponseEntity<ProfileDTO> update(@RequestParam("id") Integer id,
                                             @RequestBody ProfileDTO dto,
                                             @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.update(id, dto, jwtDTO.getId()));
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam("id") Integer id,
                                         @RequestBody ProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.update(id, dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("id") Integer id,
                                         @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.delete(id, jwtDTO.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getById(@PathVariable("id") Integer id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProfileDTO> deleteById(@PathVariable("id") Integer id) {
        return null;
    }


    @GetMapping(value = "/pagination")
    public ResponseEntity<?> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "4") int size) {
        Page<ProfileDTO> pagination = profileService.pagination(page, size);
        return ResponseEntity.ok(pagination);
    }

    @PostMapping("/filter-1")
    public ResponseEntity<List<ProfileDTO>> getProfileWithFilter(@RequestBody @Valid ProfileFilterRequestDTO filterDTO) {
        List<ProfileDTO> dtos = profileService.filter(filterDTO);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/update-photo")
    public ResponseEntity<String> updatePhotoId(@RequestParam("file-name") String fileName,
                                                @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(profileService.updatePhotoId(fileName, jwtDTO.getId()));
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileDTO>> getAll() {
        return null;
    }

}
