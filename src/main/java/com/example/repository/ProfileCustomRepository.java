package com.example.repository;

import com.example.dto.filter.ProfileFilterRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProfileCustomRepository {
    @Autowired
    private EntityManager entityManager;

    public List filter(ProfileFilterRequestDTO filterDTO) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        builder.append("Select p from ProfileEntity as p where visible = true");
        if (filterDTO.getName() != null) {
            builder.append(" and p.name like :name");
            params.put("name", "%" + filterDTO.getName() + "%");
        }
        if (filterDTO.getSurname() != null) {
            builder.append(" and p.surName like :surName");
            params.put("surname", "%"+filterDTO.getSurname()+"%");
        }
        if (filterDTO.getEmail() != null) {
            builder.append(" and p.email like :email");
            params.put("email", "%" + filterDTO.getEmail() + "%");
        }
        if (filterDTO.getPhone() != null) {
            builder.append(" and p.phone like :phone");
            params.put("phone", "%" + filterDTO.getPhone() + "%");
        }
        if (filterDTO.getPhone() != null) {
            builder.append(" and p.password like :password");
            params.put("password", "%" + filterDTO.getPassword() + "%");
        }
        if (filterDTO.getRole() != null) {
            builder.append(" and p.role =:role");
            params.put("role", filterDTO.getRole());
        }
        if (filterDTO.getCreatedDateFrom() != null && filterDTO.getCreatedDateTo() != null) {
            builder.append(" and p.createdDate between :dateFrom and :dateTo");
            params.put("dateFrom", filterDTO.getCreatedDateFrom());
            params.put("dateTo", filterDTO.getCreatedDateTo());
        } else if (filterDTO.getCreatedDateFrom() != null) {
            builder.append(" and p.createdDate >= :dateFrom");
            params.put("dateFrom", filterDTO.getCreatedDateFrom());
        }else if (filterDTO.getCreatedDateTo() != null) {
            builder.append(" and p.createdDate <= :dateTo");
            params.put("dateTo", filterDTO.getCreatedDateTo());
        }
        Query query = entityManager.createQuery(builder.toString());
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        return query.getResultList();
    }
}
