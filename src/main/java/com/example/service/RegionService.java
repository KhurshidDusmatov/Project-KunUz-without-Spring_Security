package com.example.service;


import com.example.dto.region.RegionDTO;
import com.example.entity.CategoryEntity;
import com.example.entity.RegionEntity;
import com.example.enums.LangEnum;
import com.example.exps.AppBadRequestException;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDTO create(RegionDTO dto, Integer adminId) {
        RegionEntity entity = new RegionEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setVisible(true);
        entity.setPrtId(adminId);
        regionRepository.save(entity);
        dto.setId(entity.getId());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public String update(Integer id, RegionDTO dto, Integer adminId) {
        RegionEntity entity = get(id);
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setPrtId(adminId);
        regionRepository.save(entity);
        return "Successfully updated";
    }

    public RegionEntity get(Integer id){
        Optional<RegionEntity> optional = regionRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Item not found");
        }
        return optional.get();
    }

    public Boolean delete(Integer id, Integer adminId) {
        regionRepository.updateVisible(id, adminId);
        return true;
    }

    public Page<RegionDTO> pagination(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<RegionEntity> entityPage = regionRepository.findAll(pageable);
        List<RegionEntity> entities = entityPage.getContent();
        List<RegionDTO> dtos = new LinkedList<>();
        entities.forEach(entity -> {
            RegionDTO dto = new RegionDTO();
            dtos.add(toDTO(entity, dto));
        });
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    private RegionDTO toDTO(RegionEntity entity, RegionDTO dto) {
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setNameEn(entity.getNameEn());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<RegionDTO> getByLanguage(String language) {
        return null;
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> iterable = regionRepository.findAll();
        List<RegionDTO> dtos = new LinkedList<>();
        iterable.forEach(regionEntity -> {
            RegionDTO dto = new RegionDTO();
            dtos.add(toDTO(regionEntity, dto));
        });
        return dtos;
    }

    public RegionDTO getByIdAndLang(Integer regionId, LangEnum lang) {
        RegionEntity entity = get(regionId);
        RegionDTO dto = new RegionDTO();
        dto.setId(entity.getId());
        switch (lang) {
            case en -> {
                dto.setName(entity.getNameEn());
            }
            case ru -> {
                dto.setName(entity.getNameRu());
            }
            case uz -> {
                dto.setName(entity.getNameUz());
            }
        }
        return dto;
    }
}
