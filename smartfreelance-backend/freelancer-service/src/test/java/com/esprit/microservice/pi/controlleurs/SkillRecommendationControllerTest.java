package com.esprit.microservice.pi.controlleurs;

import com.esprit.microservice.pi.DTO.SkillRecommendationDTO;
import com.esprit.microservice.pi.services.SkillRecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillRecommendationController.class)
class SkillRecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillRecommendationService service;

    @Test
    void shouldReturnRecommendation() throws Exception {

        SkillRecommendationDTO dto =
                new SkillRecommendationDTO("Java", List.of("Java", "Spring"), 80);

        when(service.analyzeSkills(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/profiles/1/recommendation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dominantSkill").value("Java"))
                .andExpect(jsonPath("$.globalSkillScore").value(80));
    }
}