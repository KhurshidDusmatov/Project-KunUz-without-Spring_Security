package com.example.repository;

import com.example.entity.ArticleEntity;
import com.example.enums.ArticleStatus;
import com.example.mapper.ArticleShortInfoMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends CrudRepository<ArticleEntity,String>,
        PagingAndSortingRepository<ArticleEntity, String> {

    @Transactional
    @Modifying
    @Query("update ArticleEntity  set visible = false  where id =:id")
    int deleteArticle(@Param("id") String id);
    @Transactional
    @Modifying
    @Query("update ArticleEntity  set status =:status  where id =:id")
    int changeStatus(@Param("status") ArticleStatus status, @Param("id") String id);
    List<ArticleEntity> findTop5ByTypeIdAndStatusAndVisibleOrderByCreatedDateDesc(Integer typeId,
                                                                                  ArticleStatus status,
                                                                                  Boolean visible);


    @Query(value = "SELECT a.id,a.title,a.description,a.attach_id,a.published_date " +
            " FROM article AS a  where  a.type_id =:typeId and status =:status order by created_date desc Limit :limit",
            nativeQuery = true)
    List<ArticleShortInfoMapper> find5ByTypeIdNative(@Param("typeId") Integer typeId,
                                                     @Param("status") String status,
                                                     @Param("limit") Integer limit);
    @Query("From ArticleEntity where status =:status and visible = true and typeId =:typeId order by createdDate desc limit 5")
    List<ArticleEntity> find5ByTypeId(@Param("typeId") Integer typeId, @Param("status") ArticleStatus status);

    @Query("SELECT new ArticleEntity(id,title,description,attachId,publishedDate) From ArticleEntity where status =:status and visible = true and typeId =:typeId order by createdDate desc limit 5")
    List<ArticleEntity> find5ByTypeId2(@Param("typeId") Integer typeId, @Param("status") ArticleStatus status);

    @Query(value = "SELECT a.id,a.title,a.description,a.attach_id,a.published_date " +
            " FROM article AS a  where  a.article_type_id =:t_id and status =:status order by created_date desc Limit :limit",
            nativeQuery = true)
    List<ArticleShortInfoMapper> getTopN(@Param("t_id") Integer t_id, @Param("status") String status, @Param("limit") Integer limit);

    @Query("From ArticleEntity where status =:status and visible = true  order by createdDate desc")
    List<ArticleEntity> getAll(@Param("status") ArticleStatus status);
    @Query("From ArticleEntity where id =:id and status =:status and visible = true ")
    Optional<ArticleEntity> getById(@Param("id") String id, @Param("status") ArticleStatus status);

    @Query("From ArticleEntity where status =:status and visible = true order by viewCount desc limit 4")
    List<ArticleEntity> get4ByMostRead(@Param("status") ArticleStatus status);

    @Query("From ArticleEntity where typeId =:typeId and regionId =:regionId and  status =:status and visible = true order by createdDate desc limit 5")
    List<ArticleEntity> getAllByTypeAndRegion(@Param("typeId") Integer typeId, @Param("regionId") Integer regionId, @Param("status") ArticleStatus status);
    Page<ArticleEntity> findAllByRegionId(Integer regionId, Pageable pageable);

    @Query("From ArticleEntity where categoryId =:categoryId and status =:status and visible = true order by createdDate desc limit 5")
    List<ArticleEntity> getAllByCategory(@Param("categoryId") Integer categoryId, @Param("status") ArticleStatus status);

    Page<ArticleEntity> findAllByCategoryId(Integer categoryId, Pageable pageable);

    @Query("From ArticleEntity where status =:status and visible = true and id not in :list order by createdDate desc limit 8")
    List<ArticleEntity> get8ByExceptList(@Param("status") ArticleStatus status,
                                         @Param("list") List<String> idList);

}
