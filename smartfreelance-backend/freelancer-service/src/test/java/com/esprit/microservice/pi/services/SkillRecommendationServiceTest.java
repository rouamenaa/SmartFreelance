package com.esprit.microservice.pi.services;

import com.esprit.microservice.pi.DTO.SkillRecommendationDTO;
import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.entites.Skill;
import com.esprit.microservice.pi.repositories.FreelancerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillRecommendationServiceTest {

    @Mock
    private FreelancerRepository repository;

    @InjectMocks
    private SkillRecommendationService service;

    private FreelancerProfile profile;

    @BeforeEach
    void setup() {
        profile = new FreelancerProfile();

        Skill s1 = new Skill();
        s1.setName("Java");
        s1.setLevel("Expert");

        Skill s2 = new Skill();
        s2.setName("Angular");
        s2.setLevel("Beginner");

        profile.setSkills(List.of(s1, s2));
    }

    @Test
    void shouldAnalyzeSkills_success() {

        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(profile));

        // WHEN
        SkillRecommendationDTO result = service.analyzeSkills(1L);

        // THEN
        assertNotNull(result);
        assertEquals("Java", result.getDominantSkill());
        assertTrue(result.getTopSkills().contains("Java"));
        assertTrue(result.getGlobalSkillScore() > 0);
    }

    @Test
    void shouldReturnEmpty_whenNoSkills() {

        profile.setSkills(Collections.emptyList());

        when(repository.findById(1L)).thenReturn(Optional.of(profile));

        SkillRecommendationDTO result = service.analyzeSkills(1L);

        assertEquals(0, result.getGlobalSkillScore());
        assertTrue(result.getTopSkills().isEmpty());
    }

    @Test
    void shouldThrowException_whenProfileNotFound() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.analyzeSkills(1L);
        });
    }
}