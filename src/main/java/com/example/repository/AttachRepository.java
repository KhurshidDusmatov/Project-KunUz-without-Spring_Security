package com.example.repository;

import com.example.entity.AttachEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachRepository extends PagingAndSortingRepository<AttachEntity, String>, CrudRepository<AttachEntity, String> {
}
