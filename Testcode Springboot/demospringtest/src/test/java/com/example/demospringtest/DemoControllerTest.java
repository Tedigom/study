package com.example.demospringtest;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@AutoConfigureMockMvc
@AutoConfigureWebTestClient
public class DemoControllerTest {

    // ######### Mock MVC Version ##########
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void helloWorldTest() throws Exception{
//        mockMvc.perform(get("/"))           // "/"로 get요청이 들어왔을때
//                .andDo(print())
//                .andExpect(status().isOk())             // http 상태코드가 isOk()인지 확인하고
//                .andExpect(content().string(containsString("Hello World")));       // content(), 즉 body안에 Hello world라는 문자가 있는지 확인
//    }


    @Autowired
    private WebTestClient webClient;

    @Test
    public void helloWorldTest() throws Exception{
        webClient.get().uri("/").exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello World");
    }
}
