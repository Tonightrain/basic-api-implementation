package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
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

    @PostMapping("/User")
    public ResponseEntity registerUser(@RequestBody @Valid User user){
         userList.add(user);
         return ResponseEntity.created(null).build();
    }

    @GetMapping("/Users")
    public ResponseEntity getUserList(){
        return ResponseEntity.ok(userList);
    }
}
