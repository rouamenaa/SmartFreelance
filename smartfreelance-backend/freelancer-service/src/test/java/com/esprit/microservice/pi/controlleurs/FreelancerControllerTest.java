package com.esprit.microservice.pi.controlleurs;

import com.esprit.microservice.pi.DTO.ProfileAnalyticsDTO;
import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.entites.Skill;
import com.esprit.microservice.pi.services.CvPdfService;
import com.esprit.microservice.pi.services.FreelancerService;
import com.esprit.microservice.pi.services.ProfileViewNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FreelancerController.class)
class FreelancerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FreelancerService service;

    @MockBean
    private CvPdfService cvPdfService;

    @MockBean
    private ProfileViewNotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetProfile() throws Exception {

        FreelancerProfile profile = new FreelancerProfile();
        profile.setFirstName("John");

        when(service.getProfile(1L)).thenReturn(profile);

        mockMvc.perform(get("/api/profile/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldAddSkill() throws Exception {

        Skill skill = new Skill();
        skill.setName("Java");

        when(service.addSkill(eq(1L), any())).thenReturn(skill);

        mockMvc.perform(post("/api/profile/1/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skill)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"));
    }

    @Test
    void shouldGetAnalytics() throws Exception {

        ProfileAnalyticsDTO dto =
                new ProfileAnalyticsDTO(2, 1, BigDecimal.valueOf(50), 3);

        when(service.getProfileAnalytics(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/profile/1/analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSkills").value(2));
    }

    @Test
    void shouldDownloadCv() throws Exception {

        when(cvPdfService.generateCvPdf(1L)).thenReturn(new byte[10]);

        mockMvc.perform(get("/api/profile/1/cv"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Disposition"));
    }
}