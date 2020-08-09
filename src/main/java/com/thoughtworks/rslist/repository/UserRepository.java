package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity,Integer> {
    List<UserEntity> findAll();
    void deleteById(Integer id);

    Page<UserEntity> findAll(Pageable pageable);
}
