package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.Table;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RsControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        init();

    }

    public void init(){
        UserEntity user1 = UserEntity.builder().name("Mike").gender("male").age(18).email("805560811@qq.com").phone("13885689666").voteNum(10).build();
        userRepository.save(user1);
        RsEventEntity rsEventEntity1 = RsEventEntity.builder().eventName("事件一").keyWord("分类一").userId(String.valueOf(user1.getId())).build();
        rsEventRepository.save(rsEventEntity1);
        UserEntity user2 = UserEntity.builder().name("Jack").gender("female").age(20).email("905560811@qq.com").phone("15555555555").voteNum(10).build();
        userRepository.save(user2);
        RsEventEntity rsEventEntity2 = RsEventEntity.builder().eventName("事件二").keyWord("分类二").userId(String.valueOf(user2.getId())).build();
        rsEventRepository.save(rsEventEntity2);
    }

    @Test
    void shouldGetRsList() throws Exception{
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName",is("事件一")))
                .andExpect((jsonPath("$[0].keyWord",is("分类一"))))
                .andExpect(jsonPath("$[1].eventName",is("事件二")))
                .andExpect((jsonPath("$[1].keyWord",is("分类二"))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRsEventBetween() throws Exception{
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$[0].eventName",is("事件一")))
                .andExpect(jsonPath("$[0].keyWord",is("分类一")))
                .andExpect(jsonPath("$[0]",not(hasKey("userEntity"))))
                .andExpect(jsonPath("$[1].eventName",is("事件二")))
                .andExpect(jsonPath("$[1].keyWord",is("分类二")))
                .andExpect(jsonPath("$[1]",not(hasKey("userEntity"))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOneRsEvent() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("事件一")))
                .andExpect(jsonPath("$.keyWord",is("分类一")))
                .andExpect(jsonPath("$",not(hasKey("userEntity"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName",is("事件二")))
                .andExpect(jsonPath("$.keyWord",is("分类二")))
                .andExpect(jsonPath("$",not(hasKey("userEntity"))))
                .andExpect(status().isOk());
    }

    @Test
    void indexShouldValidWhenGetOneRsEvent() throws Exception{
        mockMvc.perform(get("/rs/10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddOneRsEvent() throws Exception{
        UserEntity user = UserEntity.builder().name("Mike").gender("male").age(18).email("805560811@qq.com").phone("13667899265").build();
        user = userRepository.save(user);
        String userId = String.valueOf(user.getId());
        String rsJson = "{\"eventName\":\"事件三\",\"keyWord\":\"分类三\",\"userId\":"+userId+"}";
        mockMvc.perform(post("/rs/event").content(rsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(3,rsEventRepository.count());
    }

    @Test
    void shouldUserOfAddRsEventBeRegistered() throws Exception{
        String userId = String.valueOf(userRepository.count()+5);
        String rsJson = "{\"eventName\":\"第一条事件\",\"keyWord\":\"无分类\",\"userId\":"+userId+"}";
        mockMvc.perform(post("/rs/event").content(rsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldDeleteOneRsEvent() throws Exception{
        mockMvc.perform(post("/rs/delete/1"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName",is("事件二")))
                .andExpect(jsonPath("$[0].keyWord",is("分类二")))
                .andExpect(jsonPath("$[0]",not(hasKey("userEntity"))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldModifyOneRsEvent1() throws Exception{
        RsEvent rsEvent = new RsEvent("修改第一个事件","改动一","1",0);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/modify/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventEntity> rs = rsEventRepository.findAll();
        assertEquals("修改第一个事件",rs.get(0).getEventName());
        assertEquals("改动一",rs.get(0).getKeyWord());
        //assertEquals("1",rs.get(0).getUserId());
    }

    @Test
    void UserIdShouldMatchRsEventUserIdWhenModifyRsEvent() throws Exception{
        RsEvent rsEvent = new RsEvent("修改第一个事件","改动一","2",0);
        RsEvent rsEvent1 = new RsEvent("修改第二个事件","改动二","null",0);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString1 = objectMapper.writeValueAsString(rsEvent);
        String jsonString2 = objectMapper.writeValueAsString(rsEvent1);

        mockMvc.perform(post("/rs/modify/1").content(jsonString1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/rs/modify/2").content(jsonString2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldModifyOneRsEvent2() throws Exception{
        RsEvent rsEvent = new RsEvent("修改第一个事件",null,"1",0);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/modify/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventEntity> rs = rsEventRepository.findAll();
        assertEquals("修改第一个事件",rs.get(0).getEventName());
        assertEquals("分类一",rs.get(0).getKeyWord());
        assertEquals("1",rs.get(0).getUserId());
    }

    @Test
    void shouldModifyOneRsEvent3() throws Exception{
        RsEvent rsEvent = new RsEvent(null,"改动一","1",0);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/modify/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventEntity> rs = rsEventRepository.findAll();
        assertEquals("事件一",rs.get(0).getEventName());
        assertEquals("改动一",rs.get(0).getKeyWord());
        assertEquals("1",rs.get(0).getUserId());
    }

//    @Test
//    void userShouldValid() throws Exception{
//        String requestJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"无分类\",\"user\":{\"name\":\"Jakeeeeee\",\"gender\":\"female\",\"age\":35,\"email\":\"505560811@qq.com\",\"phone\":\"13667899265\"}}";
//        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error",is("invalid user")));
//    }
}