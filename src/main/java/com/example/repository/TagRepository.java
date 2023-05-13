package com.example.repository;

import com.example.entity.TagEntity;
import jakarta.transaction.Transactional;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<TagEntity, Integer> {

    @Transactional
    @Modifying
    @Query("update TagEntity  set visible = false , prtId =:prtId where id =:id")
    int updateVisible(@Param("id") Integer id, @Param("prtId") Integer prtId);

    @Query("from TagEntity where visible = true ")
    List<TagEntity> getAll();
}
