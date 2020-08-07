package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;



@RestController
public class UserController {
    public static List<User> userList;

    @Autowired
   UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Valid User user){
        UserEntity userEntity = UserEntity.builder().name("Mike").gender("male").age(18).email("805560811@qq.com").phone("13667899265").build();

        UserEntity userEntity1 = UserEntity.builder().name("Darcy").gender("female").age(25).email("125560811@qq.com").phone("15887899265").build();

        userRepository.save(userEntity);

        userRepository.save(userEntity1);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/users")
    public ResponseEntity getUserList(){
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getOneUser(@PathVariable Integer id){
        UserEntity userEntity1 = UserEntity.builder().name("John").gender("male").age(28).email("655560811@qq.com").phone("16787899265").build();
        userRepository.save(userEntity1);
        return ResponseEntity.ok(userRepository.findById(id));
    }

    @PostMapping("/user/delete/{id}")
    public ResponseEntity deleteOneUser(@PathVariable Integer id){
        rsEventRepository.deleteByUserId(id);
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
