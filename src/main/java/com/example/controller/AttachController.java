package com.example.controller;

import com.example.dto.attach.AttachDTO;
import com.example.enums.ProfileRole;
import com.example.service.AttachService;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        AttachDTO res = attachService.saveToSystem3(file);
        return ResponseEntity.ok().body(res);
    }
    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] open(@PathVariable("fileName") String fileName) {
        if (fileName != null && fileName.length() > 0) {
            try {
                return this.attachService.loadImage2(fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
        return null;
    }
    @GetMapping(value = "/load/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] load(@PathVariable("fileName") String fileName) {
        if (fileName != null && fileName.length() > 0) {
            try {
                return this.attachService.loadImage2(fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
        return null;
    }
    @GetMapping(value = "/open-general/{fileName}", produces = MediaType.ALL_VALUE)
    public byte[] open_general(@PathVariable("fileName") String fileName) {
        return attachService.openGeneral2(fileName);
    }

    @GetMapping("/download/{fineName}")
    public ResponseEntity<Resource> download(@PathVariable("fineName") String fileName) {
        Resource file = attachService.download(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @DeleteMapping("/delete/{fineName}")
    public ResponseEntity<Boolean> delete(@PathVariable("fineName") String fileName) {
        boolean delete = attachService.delete(fileName);
        return ResponseEntity.ok(delete);
    }

    @GetMapping(value = "/pagination")
    public ResponseEntity<?> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "4") int size,
                                        @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        Page<AttachDTO> pagination = attachService.pagination(page, size);
        return ResponseEntity.ok(pagination);
    }
    // This method for only image
//    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
//    public byte[] open(@PathVariable("fileName") String fileName) {
//        if (fileName != null && fileName.length() > 0) {
//            try {
//                return this.attachService.open(fileName);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return new byte[0];
//            }
//        }
//        return null;
//    }

}
