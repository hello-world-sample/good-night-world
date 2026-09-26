package com.example.goodnightworld;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GoodNightController.class)
@TestPropertySource(properties = "greeting.name=Adam")
class GoodNightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void goodNightReturnsGoodNightAdam() throws Exception {
        mockMvc.perform(get("/api/good-night"))
                .andExpect(status().isOk())
                .andExpect(content().string("Good night Adam"));
    }
}
