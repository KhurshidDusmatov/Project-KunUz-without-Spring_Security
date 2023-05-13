package com.example.service;

import com.example.dto.profile.ProfileDTO;
import com.example.dto.profile.ProfileShortInfoDTO;
import com.example.dto.profile.ProfileUpdateDTO;
import com.example.dto.filter.ProfileFilterRequestDTO;
import com.example.entity.AttachEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.GeneralStatus;
import com.example.exps.AppBadRequestException;
import com.example.repository.AttachRepository;
import com.example.repository.ProfileCustomRepository;
import com.example.repository.ProfileRepository;
import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileCustomRepository profileCustomRepository;
    @Autowired
    private AttachRepository attachRepository;
    @Autowired
    private AttachService attachService;

    public ProfileDTO create(ProfileDTO dto, Integer adminId) {
        // check - homework
        isValidProfile(dto);

        ProfileEntity entity = new ProfileEntity();
        return setDetailToEntity(entity, dto, adminId);
    }

    public void isValidProfile(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndPasswordAndVisible(dto.getEmail(),
                dto.getPassword(),
                true);
        if (optional.isPresent()) {
            throw new AppBadRequestException("This profile already exist");
        }
    }

    public ProfileDTO update(Integer id, ProfileDTO dto, Integer adminId) {
        ProfileEntity entity = get(id);
        return setDetailToEntity(entity, dto, adminId);
    }

    public String update(Integer id, ProfileUpdateDTO dto){
        ProfileEntity entity = get(id);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(dto.getPassword());
        entity.setPassword(dto.getPassword());
        entity.setEmail(dto.getEmail());
        profileRepository.save(entity);
        return "Successfully updated";
    }

    public ProfileEntity get(Integer id){
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Profile not found");
        }
        return optional.get();
    }

    private ProfileDTO setDetailToEntity(ProfileEntity entity, ProfileDTO dto, Integer adminId){
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setPassword(MD5Util.getMd5Hash(dto.getPassword())); // MD5 ?
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setPrtId(adminId);
        entity.setStatus(GeneralStatus.ACTIVE);
        profileRepository.save(entity);
        dto.setPassword(null);
        dto.setId(entity.getId());
        return dto;
    }

    public Page<ProfileDTO> pagination(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ProfileEntity> entityPage = profileRepository.findAll(pageable);
        List<ProfileEntity> entities = entityPage.getContent();
        List<ProfileDTO> dtos = new LinkedList<>();
        entities.forEach(entity -> {
            dtos.add(toDTO(entity));
        });
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    public ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        return dto;
    }

    public ProfileShortInfoDTO toShortInfo(ProfileEntity entity) {
        ProfileShortInfoDTO dto = new ProfileShortInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        return dto;
    }

    public String delete(Integer id, Integer adminId) {
        ProfileEntity entity = get(id);
        entity.setVisible(false);
        entity.setPrtId(adminId);
        profileRepository.save(entity);
        return "Profile deleted";
    }

    public List<ProfileDTO> filter(ProfileFilterRequestDTO filterDTO) {
        List<ProfileEntity> entities = profileCustomRepository.filter(filterDTO);
        List<ProfileDTO> dtos = new ArrayList<>();
        entities.forEach(entity -> {
            ProfileDTO dto = new ProfileDTO();
            dtos.add(toDTO(entity));
        });
        return dtos;
    }

    public String updatePhotoId(String fileName, Integer currentProfileId) {
        ProfileEntity entity = get(currentProfileId);
        int lastIndex = fileName.lastIndexOf(".");
        String id = fileName.substring(0,lastIndex);
        AttachEntity attachEntity = attachService.get(id);
        entity.setAttach(attachEntity);
        profileRepository.save(entity);
        return "Successfully updated";
    }
}
