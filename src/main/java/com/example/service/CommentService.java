package com.example.service;

import com.example.dto.comment.CommentFilterDTO;
import com.example.dto.comment.CommentRequestDTO;
import com.example.dto.comment.CommentUpdateRequestDTO;
import com.example.dto.comment.CommentResponseDTO;
import com.example.entity.CommentEntity;
import com.example.enums.ProfileRole;
import com.example.exps.ItemNotFoundException;
import com.example.exps.MethodNotAllowedException;
import com.example.repository.CommentCustomRepository;
import com.example.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentCustomRepository commentCustomRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ProfileService profileService;

    public CommentRequestDTO create(CommentRequestDTO dto, Integer ownerId) {
        CommentEntity entity = new CommentEntity();
        entity.setArticleId(dto.getArticleId());
        entity.setOwnerId(ownerId);
        entity.setContent(dto.getContent());
        entity.setVisible(true);
        entity.setReplyId(dto.getReplyId());
        entity.setCreatedDate(LocalDateTime.now());
        commentRepository.save(entity);
        return dto;
    }

    public Boolean update(Integer commentId, Integer ownerId, CommentUpdateRequestDTO dto) {
        Optional<CommentEntity> optional = commentRepository.findByIdAndOwnerId(commentId, ownerId);
        if (optional.isEmpty()) {
            throw new MethodNotAllowedException("You have not allow");
        }
        CommentEntity entity = optional.get();
        entity.setContent(dto.getContent());
        entity.setArticleId(dto.getArticleId());
        entity.setUpdateDate(LocalDateTime.now());
        commentRepository.save(entity);
        return true;
    }

    public Boolean delete(Integer id, Integer ownerId, ProfileRole role) {
        Optional<CommentEntity> optional = commentRepository.findByIdAndOwnerId(id, ownerId);
        if (optional.isPresent() || role.equals(ProfileRole.ADMIN)) {
            commentRepository.updateVisible(id);
            return true;
        }
        return false;
    }

    public List<CommentResponseDTO> getListByArticleId(String articleId) {
        List<CommentEntity> all = commentRepository.findAllByArticleId(articleId);
        if (all.isEmpty()) {
            throw new ItemNotFoundException("Anything not found ");
        }
       return toShortDTO(all);
    }

    public List<CommentResponseDTO> toDTO(List<CommentEntity> entities){
        List<CommentResponseDTO> dtos = new ArrayList<>();
        entities.stream().forEach(entity -> {
            CommentResponseDTO dto = new CommentResponseDTO();
            dto.setId(entity.getId());
            dto.setContent(entity.getContent());
            dto.setArticle(articleService.toArticleShortInfo(entity.getArticle()));
            dto.setProfile(profileService.toShortInfo(entity.getOwner()));
            dto.setReplyId(entity.getReplyId());
            dto.setUpdateDate(entity.getUpdateDate());
            dto.setCreatedDate(entity.getCreatedDate());
            dtos.add(dto);
        });
        return dtos;
    }

    public List<CommentResponseDTO> toShortDTO(List<CommentEntity> entities){
        List<CommentResponseDTO> dtos = new ArrayList<>();
        entities.stream().forEach(entity -> {
            CommentResponseDTO dto = new CommentResponseDTO();
            dto.setId(entity.getId());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setUpdateDate(entity.getUpdateDate());
            dto.setProfile(profileService.toShortInfo(entity.getOwner()));
            dtos.add(dto);
        });
        return dtos;
    }

    public Page<CommentResponseDTO> pagination(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<CommentEntity> entityPage = commentRepository.findAll(pageable);
        List<CommentEntity> entities = entityPage.getContent();
        List<CommentResponseDTO> dtos = toDTO(entities);
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    public PageImpl<CommentResponseDTO> filter(CommentFilterDTO filterDTO, int page, int size) {
        Page<CommentEntity> pageObj = commentCustomRepository.filter(filterDTO, page, size);
        List<CommentResponseDTO> dtoList = new LinkedList<>();
        pageObj.forEach(entity -> {
            CommentResponseDTO dto = new CommentResponseDTO();
            dto.setId(entity.getId());
            dto.setContent(entity.getContent());
            dto.setArticle(articleService.toArticleShortInfo(entity.getArticle()));
            dto.setProfile(profileService.toShortInfo(entity.getOwner()));
            dto.setReplyId(entity.getReplyId());
            dto.setUpdateDate(entity.getUpdateDate());
            dto.setCreatedDate(entity.getCreatedDate());
            dtoList.add(dto);
        });
        return new PageImpl<>(dtoList, PageRequest.of(page, size), pageObj.getTotalElements());
    }

    public List<CommentResponseDTO> getListByCommentId(Integer id) {
        List<CommentEntity> all = commentRepository.getByReplyId(id);
        if (all.isEmpty()) {
            throw new ItemNotFoundException("Anything not found ");
        }
        return toShortDTO(all);
    }


}
