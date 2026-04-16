package com.esprit.microservice.pi.controlleurs;

import com.esprit.microservice.pi.DTO.ProfileAnalyticsDTO;
import com.esprit.microservice.pi.services.ProfileAnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileAnalyticsController.class)
class ProfileAnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileAnalyticsService service;

    @Test
    void shouldReturnAnalytics() throws Exception {

        ProfileAnalyticsDTO dto =
                new ProfileAnalyticsDTO(3, 2, BigDecimal.valueOf(100), 5);

        when(service.getAnalytics(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/profiles/1/analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSkills").value(3))
                .andExpect(jsonPath("$.totalProjects").value(2));
    }
}