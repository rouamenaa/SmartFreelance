package com.esprit.microservice.pi.controlleurs;

import com.esprit.microservice.pi.DTO.ChatbotResponseDTO;
import com.esprit.microservice.pi.services.ChatbotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatbotController.class)
class ChatbotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatbotService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRecommendFreelancers() throws Exception {

        ChatbotResponseDTO dto =
                new ChatbotResponseDTO("John Doe", List.of("Java"), 4.5, 90.0);

        when(service.recommendFreelancers("Java developer"))
                .thenReturn(List.of(dto));

        Map<String, String> request = Map.of("message", "Java developer");

        mockMvc.perform(post("/api/chatbot/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].matchingScore").value(90));
    }
}