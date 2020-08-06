package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void cleanUp(){
//        UserController.userList = new ArrayList<>();
//        UserController.userList.add(new User("Mike", "male",18,"805560811@qq.com","13667899265"));
//        UserController.userList.add(new User("Darcy", "female",25,"125560811@qq.com","15887899265"));
//        UserController.userList.add(new User("John", "male",30,"655560811@qq.com","16787899265"));
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        User user = new User("Mike", "male",18,"805560811@qq.com","13667899265");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserEntity> users = userRepository.findAll();
        assertEquals(1,users.size());
        assertEquals("Mike",users.get(0).getName());
    }


    @Test
    void nameShouldLessThan8() throws Exception{
        User user = new User("Mikeeeeee", "male",18,"805560811@qq.com","13667899265");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }


    @Test
    void nameShouldNotNull() throws Exception{
        User user = new User(null, "female",18,"805560811@qq.com","13667899265");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void genderShouldNotNull() throws Exception{
        User user = new User("Mike", null,18,"805560811@qq.com","13667899265");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void ageShouldNotLessThan18() throws Exception{
        User user = new User("Mike", "female",17,"805560811@qq.com","13667899265");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void ageShouldNotMoreThan100() throws Exception{
        User user = new User("Mike", "female",120,"805560811@qq.com","13667899265");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void emailShouldValid() throws Exception{
        User user = new User("Mike", "female",120,"8055608@qq.com","13667899265");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void phoneShouldValid() throws Exception{
        User user = new User("Mike", "female",120,"805560812@qq.com","23667899265");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void getUserList() throws Exception{
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].name", is("Mike")))
                .andExpect(jsonPath("$[0].gender", is("male")))
                .andExpect(jsonPath("$[0].age", is(18)))
                .andExpect(jsonPath("$[0].email", is("805560811@qq.com")))
                .andExpect(jsonPath("$[0].phone", is("13667899265")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOneUser() throws Exception{
        User user = new User("Darcy", "female",25,"125560811@qq.com","15887899265");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);


        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserEntity> userEntityList = userRepository.findAll();

        mockMvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.name",is("Mike")))
                .andExpect(jsonPath("$.age",is(18)))
                .andExpect(jsonPath("$.email",is("805560811@qq.com")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/2"))
                .andExpect(jsonPath("$.name",is("Darcy")))
                .andExpect(jsonPath("$.age",is(25)))
                .andExpect(jsonPath("$.email",is("125560811@qq.com")))
                .andExpect(status().isOk());
    }
}