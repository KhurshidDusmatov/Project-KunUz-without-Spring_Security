package com.example.service;

import com.example.dto.article.ArticleFilterDTO;
import com.example.dto.article.ArticleFullInfoDTO;
import com.example.dto.article.ArticleRequestDTO;
import com.example.dto.article.ArticleShortInfoDTO;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.enums.LangEnum;
import com.example.exps.AppBadRequestException;
import com.example.exps.ItemNotFoundException;
import com.example.mapper.ArticleShortInfoMapper;
import com.example.repository.ArticleCustomRepository;
import com.example.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ArticleTypeService articleTypeService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleCustomRepository articleCustomRepository;


    public ArticleRequestDTO create(ArticleRequestDTO dto, Integer moderId) {
        // check
//        ProfileEntity moderator = profileService.get(moderId);
//        RegionEntity region = regionService.get(dto.getRegionId());
//        CategoryEntity category = categoryService.get(dto.getCategoryId());

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setModeratorId(moderId);
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setTypeId(dto.getArticleTypeId());
        entity.setAttachId(dto.getAttachId());
        // type
        articleRepository.save(entity);
        return dto;
    }

    public ArticleRequestDTO update(ArticleRequestDTO dto, String id) {
        ArticleEntity entity = get(id);
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setTypeId(dto.getArticleTypeId());
        entity.setAttachId(dto.getAttachId());
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        articleRepository.save(entity);
        return dto;
    }

    public Boolean delete(String  id) {
      articleRepository.deleteArticle(id);
        return true;
    }

    public ArticleEntity get(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Item not found: " + id);
        }
        return optional.get();
    }


    public Boolean changeStatus(ArticleStatus status, String id, Integer prtId) {
        ArticleEntity entity = get(id);
        if (status.equals(ArticleStatus.PUBLISHED)) {
            entity.setPublishedDate(LocalDateTime.now());
            entity.setPublisherId(prtId);
        }
        entity.setStatus(status);
        articleRepository.save(entity);
        // articleRepository.changeStatus(status, id);
        return true;
    }


    public ArticleShortInfoDTO toArticleShortInfo(ArticleEntity entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setImage(attachService.getAttachLink(entity.getAttachId()));
        return dto;
    }
    public ArticleShortInfoDTO toArticleShortInfo(ArticleShortInfoMapper entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublished_date());
        dto.setImage(attachService.getAttachLink(entity.getAttachId()));
        return dto;
    }

    public List<ArticleShortInfoDTO> getLast5ByTypeId(Integer typeId) {
        List<ArticleShortInfoMapper> entityList = articleRepository.find5ByTypeIdNative(
                typeId,
                ArticleStatus.PUBLISHED.toString(),
                5);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }
    public List<ArticleShortInfoDTO> getLastNByTypeId(Integer typeId, Integer limit) {
        List<ArticleShortInfoMapper> entityList = articleRepository.getTopN(
                typeId,
                ArticleStatus.PUBLISHED.toString(),
                limit);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public List<ArticleShortInfoDTO> getLast8NotGivenList(List<String> list) {
        List<ArticleEntity> all = articleRepository.get8ByExceptList(ArticleStatus.PUBLISHED, list);
        List<ArticleShortInfoDTO> result = new ArrayList<>();
        all.forEach(entity -> {
            result.add(toArticleShortInfo(entity));
        });
        return result;
    }

   public ArticleFullInfoDTO getById(String id, LangEnum langEnum) {
        ArticleEntity entity = get(id);
        if (!entity.getVisible() || !entity.getStatus().equals(ArticleStatus.PUBLISHED)) {
            throw new AppBadRequestException("Wrong article status");
        }
        return toFullDTO(entity, langEnum);
    }
    public ArticleFullInfoDTO toFullDTO(ArticleEntity entity, LangEnum langEnum) {
        ArticleFullInfoDTO dto = new ArticleFullInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setViewCount(entity.getViewCount());
        dto.setImage(attachService.getAttachLink(entity.getAttachId()));
        dto.setCategory(categoryService.getByIdAndLang(entity.getCategoryId(), langEnum));
        dto.setRegion(regionService.getByIdAndLang(entity.getCategoryId(), langEnum));
        dto.setArticleType(articleTypeService.getByIdAndLang(entity.getCategoryId(), langEnum));
        // tag_list
        return dto;
    }

     public List<ArticleShortInfoDTO> getLast4ByType(Integer typeId, String articleId){
         List<ArticleShortInfoDTO> last5ByTypeId = getLast5ByTypeId(typeId);
         List<ArticleShortInfoDTO> resultList = new ArrayList<>();
         for (ArticleShortInfoDTO item : last5ByTypeId) {
             if (!articleId.equals(item.getId())){
                 resultList.add(item);
             }
         }
         return resultList;
     }


    public List<ArticleShortInfoDTO> getMostReadArticles() {
        List<ArticleEntity> entityList = articleRepository.get4ByMostRead(
                ArticleStatus.PUBLISHED);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public List<ArticleShortInfoDTO> getArticleByTypeAndRegion(Integer typeId, Integer regionId) {
        List<ArticleEntity> all = articleRepository.getAllByTypeAndRegion(
                typeId,
                regionId,
                ArticleStatus.PUBLISHED);

        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        all.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public Page<ArticleShortInfoDTO> paginationByRegion(int page, int size, Integer regionId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ArticleEntity> entityPage = articleRepository.findAllByRegionId(regionId, pageable);
        List<ArticleEntity> entities = entityPage.getContent();
        List<ArticleShortInfoDTO> dtos = new LinkedList<>();
        entities.forEach(entity -> {
            dtos.add(toArticleShortInfo(entity));
        });
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    public List<ArticleShortInfoDTO> getArticleByCategory(Integer categoryId) {
        List<ArticleEntity> all = articleRepository.getAllByCategory(
                categoryId,
                ArticleStatus.PUBLISHED);

        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        all.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public Page<ArticleShortInfoDTO> paginationByCategory(int page, int size, Integer categoryId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ArticleEntity> entityPage = articleRepository.findAllByCategoryId(categoryId, pageable);
        List<ArticleEntity> entities = entityPage.getContent();
        List<ArticleShortInfoDTO> dtos = new LinkedList<>();
        entities.forEach(entity -> {
            dtos.add(toArticleShortInfo(entity));
        });
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    public PageImpl<ArticleShortInfoDTO> filter(ArticleFilterDTO filterDTO, int page, int size) {
        Page<ArticleEntity> pageObj = articleCustomRepository.filter(filterDTO, page, size);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        pageObj.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return new PageImpl<>(dtoList, PageRequest.of(page, size), pageObj.getTotalElements());
    }

    //    public ArticleFullInfoDTO getByIdAndLanguage(String id, String language) {
//        Optional<ArticleEntity> optional = articleRepository.getById(id, ArticleStatus.PUBLISHED);
//        if (optional.isEmpty()) {
//            throw new ItemNotFoundException("Article not found");
//        }
//        ArticleEntity entity= optional.get();
//        switch (language){
//            case "uz"->{
//                RegionEntity region = regionService.get(entity.getRegionId());
//                RegionResponseDTO regionResponseDTO = new RegionResponseDTO();
//                regionResponseDTO.setId(region.getId());
//                regionResponseDTO.setName(region.getNameUz());
//
//                CategoryEntity category = categoryService.get(entity.getCategoryId());
//                CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
//                categoryResponseDTO.setId(category.getId());
//                categoryResponseDTO.setName(category.getNameUz());
//                return toFullInfoDTO(entity, regionResponseDTO, categoryResponseDTO);
//            }case "ru"->{
//                RegionEntity region = regionService.get(entity.getRegionId());
//                RegionResponseDTO regionResponseDTO = new RegionResponseDTO();
//                regionResponseDTO.setId(region.getId());
//                regionResponseDTO.setName(region.getNameRu());
//
//                CategoryEntity category = categoryService.get(entity.getCategoryId());
//                CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
//                categoryResponseDTO.setId(category.getId());
//                categoryResponseDTO.setName(category.getNameRu());
//                return toFullInfoDTO(entity, regionResponseDTO, categoryResponseDTO);
//            }case "eng"->{
//                RegionEntity region = regionService.get(entity.getRegionId());
//                RegionResponseDTO regionResponseDTO = new RegionResponseDTO();
//                regionResponseDTO.setId(region.getId());
//                regionResponseDTO.setName(region.getNameEn());
//
//                CategoryEntity category = categoryService.get(entity.getCategoryId());
//                CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
//                categoryResponseDTO.setId(category.getId());
//                categoryResponseDTO.setName(category.getNameEn());
//                return toFullInfoDTO(entity, regionResponseDTO, categoryResponseDTO);
//            }
//            default -> {
//                throw new MethodNotAllowedException("Language not found");
//            }
//        }
//    }
//
//     public ArticleFullInfoDTO toFullInfoDTO(ArticleEntity entity,
//                                             RegionResponseDTO regionResponseDTO,
//                                             CategoryResponseDTO categoryResponseDTO){
//
//         ArticleFullInfoDTO dto = new ArticleFullInfoDTO();
//         dto.setId(entity.getId());
//         dto.setTitle(entity.getTitle());
//         dto.setDescription(entity.getDescription());
//         dto.setContent(entity.getContent());
//         dto.setRegion(regionResponseDTO);
//         dto.setCategory(categoryResponseDTO);
//         dto.setPublishedDate(entity.getPublishedDate());
//         dto.setSharedCount(entity.getSharedCount());
//         dto.setViewCount(entity.getViewCount());
////         dto.setLikeCount(entity.getLikeCount());
//         return dto;
//     }


    //    public ArticleDTO create(ArticleDTO dto) {
//        isValidProfile(dto);
//        ArticleEntity entity = new ArticleEntity();
//        entity.setTitle(dto.getTitle());
//        entity.setDescription(dto.getDescription());
//        entity.setContent(dto.getContent());
//        entity.setSharedCount(dto.getSharedCount());
//
//        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(dto.getRegionId());
//        entity.setRegionId(optionalRegionEntity.get());
//
//        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(dto.getCategoryId());
//        entity.setCategory(optionalCategoryEntity.get());
//        entity.setStatus();(ArticleStatus.NOT_PUBLISHED);
//        articleRepository.save(entity);
//        return dto;
//    }
//    public ArticleDTO update(String id, ArticleDTO dto) {
//        Optional<ArticleEntity> optional = articleRepository.findById(id);
//        if (optional.isEmpty()) {
//            throw new AppBadRequestException(" yoqku ");
//        }
//        // isValidProfile(dto);
//        ArticleEntity entity = optional.get();
////        entity.setId(dto.getId());
//        entity.setTitle(dto.getTitle());
//        entity.setDescription(dto.getDescription());
//        entity.setContent(dto.getContent());
//        entity.setSharedCount(dto.getSharedCount());
//
//        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(dto.getRegionId());
//        entity.setRegion(optionalRegionEntity.get());
//
//        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(dto.getCategoryId());
//        entity.setCategory(optionalCategoryEntity.get());
//        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
//        articleRepository.save(entity);
//
//        dto.setId(entity.getId());
//        return dto;
//    }

//    public boolean update2(String  id, ArticleDTO dto) {
//        Optional<ArticleEntity> optional = articleRepository.findById(id);
//        if (optional.isEmpty()) {
//            throw new AppBadRequestException(" yoqku ");
//        }
//
//        if(dto.getArticleStatus().equals(ArticleStatus.NOT_PUBLISHED)){
//            ArticleEntity entity = optional.get();
//            entity.setStatus(ArticleStatus.PUBLISHED);
//            articleRepository.save(entity);
//        } else if(dto.getArticleStatus().equals(ArticleStatus.PUBLISHED)){
//            ArticleEntity entity = optional.get();
//            entity.setStatus(ArticleStatus.NOT_PUBLISHED);
//            articleRepository.save(entity);
//        }
//        return true;
//    }

//    public Page<ArticleEntity> getAll(int page, int size) {
//        return null;
//    }

//    public void isValidProfile(ArticleDTO dto) {
//        if (dto.getCategoryId() == null) {
//            throw new AppBadRequestException("invalid category");
//        }
//        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(dto.getCategoryId());
//        if (optionalCategoryEntity.isEmpty()) {
//            throw new AppBadRequestException("not found category");
//        }
//
//        if (dto.getRegionId() == null) {
//            throw new AppBadRequestException("invalid region");
//        }
//        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(dto.getRegionId());
//        if (optionalRegionEntity.isEmpty()) {
//            throw new AppBadRequestException("not found region");
//        }
//        if (dto.getDescription() == null) {
//            throw new AppBadRequestException("invalid desc");
//        }
//        if (dto.getSharedCount() == null) {
//            throw new AppBadRequestException("invalid sharedCount");
//        }
//        if (dto.getContent() == null) {
//            throw new AppBadRequestException("invalid con");
//        }
//        if (dto.getTitle() == null) {
//            throw new AppBadRequestException("invalid title");
//
//        }
//    }

}
