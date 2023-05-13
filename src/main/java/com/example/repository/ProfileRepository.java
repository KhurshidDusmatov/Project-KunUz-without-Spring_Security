package com.example.repository;

import com.example.entity.ProfileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends PagingAndSortingRepository<ProfileEntity, Integer>,
        CrudRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByEmailAndPasswordAndVisible(String email, String password, boolean visible);

    Optional<ProfileEntity> findByEmail(String email);

}
