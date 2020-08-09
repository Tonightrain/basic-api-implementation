package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventEntity,Integer> {
    List<RsEventEntity> findAll();
    @Transactional
    List<RsEventEntity> deleteByUserId(String userId);

    Page<RsEventEntity> findAll(Pageable pageable);
}
