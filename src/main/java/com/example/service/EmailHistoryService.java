package com.example.service;

import com.example.dto.EmailHistoryDTO;
import com.example.entity.EmailHistoryEntity;
import com.example.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public List<EmailHistoryDTO> getAll() {
        Iterable<EmailHistoryEntity> all = emailHistoryRepository.findAll();
        List<EmailHistoryDTO> dtos = new ArrayList<>();
        all.forEach(emailHistoryEntity -> {
            EmailHistoryDTO dto = new EmailHistoryDTO();
            dtos.add(toDTO(emailHistoryEntity, dto));
        });
        return dtos;
    }
    public List<EmailHistoryDTO> getByEmail(String email) {
        Iterable<EmailHistoryEntity> all = emailHistoryRepository.findAllByEmail(email);
        List<EmailHistoryDTO> dtos = new ArrayList<>();
        all.forEach(emailHistoryEntity -> {
            EmailHistoryDTO dto = new EmailHistoryDTO();
            dtos.add(toDTO(emailHistoryEntity, dto));
        });
        return dtos;
    }

    public List<EmailHistoryDTO> getByCreatedDate(LocalDateTime date) {
        Iterable<EmailHistoryEntity> all = emailHistoryRepository.findAllByCreatedDate(date);
        List<EmailHistoryDTO> dtos = new ArrayList<>();
        all.forEach(emailHistoryEntity -> {
            EmailHistoryDTO dto = new EmailHistoryDTO();
            dtos.add(toDTO(emailHistoryEntity, dto));
        });
        return dtos;
    }
    private EmailHistoryDTO toDTO(EmailHistoryEntity entity, EmailHistoryDTO dto) {
        dto.setId(entity.getId());
        dto.setMessage(entity.getMessage());
        dto.setEmail(entity.getEmail());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
    public Page<EmailHistoryDTO> pagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<EmailHistoryEntity> entityPage = emailHistoryRepository.findAll(pageable);
        List<EmailHistoryEntity> entities = entityPage.getContent();
        List<EmailHistoryDTO> dtos = new LinkedList<>();
        entities.forEach(entity -> {
            EmailHistoryDTO dto = new EmailHistoryDTO();
            dtos.add(toDTO(entity, dto));
        });
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }
}

