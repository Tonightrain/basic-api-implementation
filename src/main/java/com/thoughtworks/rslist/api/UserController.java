package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;



@RestController
public class UserController {
    public static List<User> userList;

    @Autowired
   UserRepository userRepository;

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Valid User user){
        UserEntity userEntity = UserEntity.builder().name("Mike").gender("male").age(18).email("805560811@qq.com").phone("13667899265").build();
        userRepository.save(userEntity);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/users")
    public ResponseEntity getUserList(){
        return ResponseEntity.ok(userList);
    }
}
