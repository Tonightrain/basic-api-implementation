package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RsControllerTest {


    @Autowired
    MockMvc mockMvc;

    @BeforeEach

//    void setUp(){
//        User user1 = new User()
//    }

    @Test
    void shouldGetRsList() throws Exception{
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect((jsonPath("$[0].keyWord",is("无分类"))))
                .andExpect(jsonPath("$[0]",hasKey("user")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect((jsonPath("$[1].keyWord",is("无分类"))))
                .andExpect(jsonPath("$[1]",hasKey("user")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect((jsonPath("$[2].keyWord",is("无分类"))))
                .andExpect(jsonPath("$[2]",hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOneRsEvent() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("第一条事件")))
                .andExpect(jsonPath("$.keyWord",is("无分类")))
                .andExpect(jsonPath("$",hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName",is("第二条事件")))
                .andExpect(jsonPath("$.keyWord",is("无分类")))
                .andExpect(jsonPath("$",hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName",is("第三条事件")))
                .andExpect(jsonPath("$.keyWord",is("无分类")))
                .andExpect(jsonPath("$",hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRsEventBetween() throws Exception{
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[0]",hasKey("user")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1]",hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$[0].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[0]",hasKey("user")))
                .andExpect(jsonPath("$[1].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1]",hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[0]",hasKey("user")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1]",hasKey("user")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无分类")))
                .andExpect(jsonPath("$[2]",hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddOneRsEvent() throws Exception{
        String requestJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"无分类\",\"user\":{\"name\":\"Jake\",\"gender\":\"female\",\"age\":35,\"email\":\"505560811@qq.com\",\"phone\":\"13667899265\"}}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[0]",hasKey("user")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1]",hasKey("user")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无分类")))
                .andExpect(jsonPath("$[3].eventName",is("第四条事件")))
                .andExpect(jsonPath("$[3]",hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteOneRsEvent() throws Exception{
        mockMvc.perform(post("/rs/delete/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[0]",hasKey("user")))
                .andExpect(jsonPath("$[1].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1]",hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldModifyOneRsEvent1() throws Exception{
        RsEvent rsEvent = new RsEvent("修改第一个事件","改动过",new User("Mike", "male",18,"805560811@qq.com","13667899265"));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/modify/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("修改第一个事件")))
                .andExpect(jsonPath("$.keyWord",is("改动过")))
                .andExpect(jsonPath("$",hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldModifyOneRsEvent2() throws Exception{
        RsEvent rsEvent = new RsEvent("修改第二个事件",null,new User("Darcy", "female",25,"125560811@qq.com","15887899265"));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/modify/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName",is("修改第二个事件")))
                .andExpect(jsonPath("$.keyWord",is("无分类")))
                .andExpect(jsonPath("$",hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldModifyOneRsEvent3() throws Exception{
        RsEvent rsEvent = new RsEvent(null,"改动过",new User("John", "male",30,"655560811@qq.com","16787899265"));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/modify/3").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName",is("第三条事件")))
                .andExpect(jsonPath("$.keyWord",is("改动过")))
                .andExpect(jsonPath("$",hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void userShouldValid() throws Exception{
        String requestJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"无分类\",\"user\":{\"name\":\"Jakeeeeee\",\"gender\":\"female\",\"age\":35,\"email\":\"505560811@qq.com\",\"phone\":\"13667899265\"}}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}