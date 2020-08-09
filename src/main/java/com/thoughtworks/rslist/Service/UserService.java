package com.thoughtworks.rslist.Service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
//    @Autowired
//    UserEntity userEntity;

    public void registerUser(User user){
        UserEntity userEntity = UserEntity.builder().name(user.getName()).gender(user.getGender()).age(user.getAge())
                .email(user.getEmail()).phone(user.getPhone()).voteNum(user.getVoteNum()).build();
        userRepository.save(userEntity);
    }

    public List<User> getUserList(Integer page,Integer size){
        List<UserEntity> userEntityList;
        if (page!=null&&size!=null&&page>0&&size>0){
            Pageable pageable = PageRequest.of(page-1,size);
            userEntityList = userRepository.findAll(pageable).getContent();
        }else {
            userEntityList = userRepository.findAll();
        }
        return userEntityList.stream().map(
                item->User.builder().name(item.getName()).gender(item.getGender()).age(item.getAge())
                .email(item.getEmail()).phone(item.getPhone()).voteNum(item.getVoteNum())
                .build()).collect(Collectors.toList());
    }

    public User getOneUser(Integer id) throws InvalidIndexException {
        if (!userRepository.existsById(id)){
            throw new InvalidIndexException("invalid index");
        }
        UserEntity userEntity= userRepository.findById(id).get();
        User user = User.builder().name(userEntity.getName()).gender(userEntity.getGender()).age(userEntity.getAge())
                .email(userEntity.getEmail()).phone(userEntity.getPhone()).voteNum(userEntity.getVoteNum()).build();
        return user;
    }

    public boolean deleteOneUser(Integer id) {
        if (!userRepository.existsById(id)){
            return false;
        }
        rsEventRepository.deleteByUserId(String.valueOf(id));
        userRepository.deleteById(id);
        return true;
    }
}
