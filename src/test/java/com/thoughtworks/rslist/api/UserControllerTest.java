package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
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

    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    void cleanUp(){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        //init();
    }

//    public void init(){
//        UserEntity userEntity = UserEntity.builder().name("Fake").gender("male").age(18).email("805560812@qq.com").phone("13667899265").voteNum(10).build();
//        userRepository.save(userEntity);
//        UserEntity userEntity1 = UserEntity.builder().name("Jack").gender("male").age(20).email("805560812@qq.com").phone("13667899265").voteNum(10).build();
//        userRepository.save(userEntity1);
//    }

    @Test
    void shouldRegisterUser() throws Exception {
        User user = new User("Mike", "male",18,"805560811@qq.com","13667899265",10);
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
        User user = new User("Mikeeeeee", "male",18,"805560811@qq.com","13667899265",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }


    @Test
    void nameShouldNotNull() throws Exception{
        User user = new User(null, "female",18,"805560811@qq.com","13667899265",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void genderShouldNotNull() throws Exception{
        User user = new User("Mike", null,18,"805560811@qq.com","13667899265",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void ageShouldNotLessThan18() throws Exception{
        User user = new User("Mike", "female",17,"805560811@qq.com","13667899265",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void ageShouldNotMoreThan100() throws Exception{
        User user = new User("Mike", "female",120,"805560811@qq.com","13667899265",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void emailShouldValid() throws Exception{
        User user = new User("Mike", "female",120,"8055608@qq.com","13667899265",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void phoneShouldValid() throws Exception{
        User user = new User("Mike", "female",120,"805560812@qq.com","23667899265",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void getUserList() throws Exception{
        UserEntity userEntity = UserEntity.builder().name("Mike").gender("male").age(18).email("805560812@qq.com").phone("13667899265").voteNum(10).build();
        userRepository.save(userEntity);
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].name", is("Mike")))
                .andExpect(jsonPath("$[0].gender", is("male")))
                .andExpect(jsonPath("$[0].age", is(18)))
                .andExpect(jsonPath("$[0].email", is("805560812@qq.com")))
                .andExpect(jsonPath("$[0].phone", is("13667899265")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOneUser() throws Exception{
        UserEntity userEntity = UserEntity.builder().name("Mike").gender("male").age(18).email("805560812@qq.com").phone("13667899265").voteNum(10).build();
        userRepository.save(userEntity);

        mockMvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.name",is("Mike")))
                .andExpect(jsonPath("$.age",is(18)))
                .andExpect(jsonPath("$.email",is("805560812@qq.com")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteOneUser() throws Exception{
        UserEntity userEntity = UserEntity.builder().name("Mike").gender("male").age(18).email("805560812@qq.com").phone("13667899265").voteNum(10).build();
        userRepository.save(userEntity);
        mockMvc.perform(post("/user/delete/1"))
                .andExpect(status().isOk());
        List<UserEntity> users = userRepository.findAll();
        assertEquals(0,userRepository.count());
    }

    @Test
    void shouldDeleteAllRsEVentWhenDeleteUser() throws Exception{
        UserEntity user = UserEntity.builder().name("Mike").gender("male").age(18).email("805560811@qq.com").phone("13885689666").voteNum(10).build();
        user = userRepository.save(user);
        UserEntity user1 = UserEntity.builder().name("Jack").gender("male").age(18).email("805560811@qq.com").phone("13885689666").voteNum(10).build();
        user1 = userRepository.save(user1);
        Integer userId = user1.getId();
        RsEventEntity rsEvent1 = RsEventEntity.builder().eventName("第一条事件").keyWord("分类一").userId(String.valueOf(userId)).build();
        RsEventEntity rsEvent2 = RsEventEntity.builder().eventName("第二条事件").keyWord("分类二").userId(String.valueOf(userId)).build();
        rsEventRepository.save(rsEvent1);
        rsEventRepository.save(rsEvent2);
        mockMvc.perform(post("/user/delete/2"))
                .andExpect(status().isOk());
        List<RsEventEntity> rs = rsEventRepository.findAll();
        assertEquals(0,rs.size());
        assertEquals(0,rsEventRepository.count());
    }

}