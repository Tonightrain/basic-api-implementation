package com.thoughtworks.rslist.Service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;


    public boolean voteRsEvent(int rsEventId, Vote vote) {
        Optional<RsEventEntity> rs = rsEventRepository.findById(rsEventId);
        Optional<UserEntity> user = userRepository.findById(vote.getUserId());
        if (!rs.isPresent()||!user.isPresent()){
            return false;
        }
        UserEntity userEntity = user.get();
        if (vote.getVoteNum()>userEntity.getVoteNum()){
            return false;
        }
        userEntity.setVoteNum(userEntity.getVoteNum()-vote.getVoteNum());
        RsEventEntity rsEventEntity = rs.get();
        rsEventEntity.setVoteNum(rsEventEntity.getVoteNum()+vote.getVoteNum());
        VoteEntity voteEntity = VoteEntity.builder().userEntity(userEntity).rsEventEntity(rsEventEntity)
                .voteNum(vote.getVoteNum()).voteTime(vote.getVoteTime()).build();
        userRepository.save(userEntity);
        rsEventRepository.save(rsEventEntity);
        voteRepository.save(voteEntity);
        return true;
    }

}
