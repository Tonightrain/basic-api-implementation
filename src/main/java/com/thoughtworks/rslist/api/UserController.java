package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.Service.UserService;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
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

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Valid User user){ ;
        userService.registerUser(user);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/users")
    public ResponseEntity getUserList(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size){
        return ResponseEntity.ok(userService.getUserList(page,size));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getOneUser(@PathVariable Integer id) throws InvalidIndexException {
        return ResponseEntity.ok(userService.getOneUser(id));
    }

    @PostMapping("/user/delete/{id}")
    public ResponseEntity deleteOneUser(@PathVariable Integer id){
        boolean flag = userService.deleteOneUser(id);
        return flag?ResponseEntity.ok().build():ResponseEntity.badRequest().build();
    }
}
