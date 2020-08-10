package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private RsEventRepository rsEventRepository;
    @Autowired
    private UserRepository userRepository;

    private UserEntity userEntity;

    private RsEventEntity rsEventEntity;

//    public void initData(){
//        UserEntity userEntity = UserEntity.builder().name("Mike").gender("male").age(18).email("805560811@qq.com").phone("13885689666").voteNum(10).build();
//        userRepository.save(userEntity);
//        RsEventEntity rsEventEntity = RsEventEntity.builder().eventName("事件一").keyWord("分类一").userId(String.valueOf(userEntity.getId())).build();
//        rsEventRepository.save(rsEventEntity);
//    }

    @BeforeEach
    void setUp(){
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void shouldVoteForRsEvent() throws Exception{
        UserEntity userEntity = UserEntity.builder().name("Mike").gender("male").age(18).email("805560811@qq.com").phone("13885689666").voteNum(10).build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder().eventName("事件一").keyWord("分类一").userEntity(userEntity).build();
        rsEventRepository.save(rsEventEntity);
        Vote vote = new Vote(userEntity.getId(),rsEventEntity.getId(),5,new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+rsEventEntity.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(1,voteRepository.count());
    }

    @Test
    void rsEventShouldNotInValid() throws Exception{
        UserEntity userEntity = UserEntity.builder().name("Mike").gender("male").age(18).email("805560811@qq.com").phone("13885689666").voteNum(10).build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder().eventName("事件一").keyWord("分类一").userEntity(userEntity).build();
        rsEventRepository.save(rsEventEntity);
        Vote vote = new Vote(userEntity.getId(),rsEventEntity.getId(),5,new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+rsEventEntity.getId()+1).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void voteNumShouldNotLessThanUserVoteNum() throws Exception{
        UserEntity userEntity = UserEntity.builder().name("Mike").gender("male").age(18).email("805560811@qq.com").phone("13885689666").voteNum(10).build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder().eventName("事件一").keyWord("分类一").userEntity(userEntity).build();
        rsEventRepository.save(rsEventEntity);
        Vote vote = new Vote(userEntity.getId(),rsEventEntity.getId(),15,new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+rsEventEntity.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}