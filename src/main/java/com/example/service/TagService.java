package com.example.service;

import com.example.dto.tag.TagDTO;
import com.example.dto.tag.TagRequestDTO;
import com.example.entity.TagEntity;
import com.example.exps.AppBadRequestException;
import com.example.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    public TagDTO create(TagRequestDTO dto, Integer creatorId) {
        TagEntity entity = new TagEntity();
        TagDTO resDto = new TagDTO();
        entity.setName(dto.getName());
        entity.setVisible(true);
        entity.setPrtId(creatorId);
        tagRepository.save(entity);
        resDto.setId(entity.getId());
        resDto.setName(entity.getName());
        return resDto;
    }

    public String update(Integer id,  TagDTO dto, Integer creatorId) {
        TagEntity entity = get(id);
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPrtId(creatorId);
        tagRepository.save(entity);
        return "Successfully updated";
    }

    public TagEntity get(Integer id){
        Optional<TagEntity> optional = tagRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Item not found");
        }
        return optional.get();
    }

    public Boolean delete(Integer id, Integer adminId) {
        tagRepository.updateVisible(id, adminId);
        return true;
    }
    public List<TagDTO> getAll(){
        List<TagEntity> entities = tagRepository.getAll();
        List<TagDTO> dtos = new ArrayList<>();
        entities.forEach(entity -> {
            TagDTO dto = new TagDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dtos.add(dto);
        });
        return dtos;
    }
}
