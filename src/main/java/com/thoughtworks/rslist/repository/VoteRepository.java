package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity,Integer> {
    List<VoteEntity> findAll();

    @Query("select v from VoteEntity v where v.voteTime > :start and v.voteTime < .end")
    List<VoteEntity> findRecordFromStartToEnd(Date start, Date end, Pageable pageable);

    @Query("select v from VoteEntity v where v.voteTime > :start and v.voteTime < .end")
    List<VoteEntity> findRecordFromStartToEnd(Date start, Date end);
}
