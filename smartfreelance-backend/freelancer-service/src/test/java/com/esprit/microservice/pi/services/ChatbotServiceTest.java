package com.esprit.microservice.pi.services;

import com.esprit.microservice.pi.DTO.ChatbotResponseDTO;
import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.entites.Skill;
import com.esprit.microservice.pi.repositories.FreelancerRepository;
import com.esprit.microservice.pi.repositories.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatbotServiceTest {

    @Mock
    private FreelancerRepository freelancerRepository;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private ChatbotService service;

    @Test
    void shouldRecommendFreelancers_whenMatchesFound() {
        // GIVEN
        FreelancerProfile f = new FreelancerProfile();
        f.setFirstName("John");
        f.setLastName("Doe");

        Skill s = new Skill();
        s.setName("Java");
        f.setSkills(List.of(s));
        f.setRating(4.5);

        when(freelancerRepository.findAll()).thenReturn(List.of(f));

        // WHEN
        List<ChatbotResponseDTO> result = service.recommendFreelancers("I need a Java developer");

        // THEN
        assertFalse(result.isEmpty());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals(100.0, result.get(0).getMatchingScore());
    }

    @Test
    void shouldReturnEmpty_whenNoSkillsExtracted() {
        // WHEN
        List<ChatbotResponseDTO> result = service.recommendFreelancers("Hello world");

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmpty_whenNoFreelancersMatch() {
        // GIVEN
        FreelancerProfile f = new FreelancerProfile();
        f.setSkills(Collections.emptyList());
        when(freelancerRepository.findAll()).thenReturn(List.of(f));

        // WHEN
        List<ChatbotResponseDTO> result = service.recommendFreelancers("I need Java");

        // THEN
        assertTrue(result.isEmpty());
    }
}
