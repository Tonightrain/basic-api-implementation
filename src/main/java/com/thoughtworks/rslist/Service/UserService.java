package com.thoughtworks.rslist.Service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    public void registerUser(User user){
        UserEntity userEntity = UserEntity.builder().name(user.getName()).gender(user.getGender()).age(user.getAge())
                .email(user.getEmail()).phone(user.getPhone()).voteNum(user.getVoteNum()).build();
        userRepository.save(userEntity);
    }
}
