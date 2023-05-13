package com.example.repository;

import com.example.entity.ArticleEntity;
import com.example.entity.EmailHistoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EmailHistoryRepository extends PagingAndSortingRepository<EmailHistoryEntity, Integer>, CrudRepository<EmailHistoryEntity,Integer> {

    Iterable<EmailHistoryEntity> findAllByEmail(String email);

    Iterable<EmailHistoryEntity> findAllByCreatedDate(LocalDateTime date);
}
