package com.esprit.microservice.pi.services;

import com.esprit.microservice.pi.DTO.SkillRecommendationRequestDTO;
import com.esprit.microservice.pi.DTO.SkillRecommendationResponseDTO;
import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.entites.Skill;
import com.esprit.microservice.pi.entites.SkillMarket;
import com.esprit.microservice.pi.repositories.FreelancerRepository;
import com.esprit.microservice.pi.repositories.SkillMarketRepository;
import com.esprit.microservice.pi.services.SkillMarketRecommendationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillMarketRecommendationServiceTest {

    @Mock
    private SkillMarketRepository marketRepo;

    @Mock
    private FreelancerRepository freelancerRepo;

    @InjectMocks
    private SkillMarketRecommendationService service;

    @Test
    void shouldRecommendSkills() {

        FreelancerProfile freelancer = new FreelancerProfile();
        Skill s = new Skill();
        s.setName("Java");
        freelancer.setSkills(List.of(s));

        SkillMarket market = new SkillMarket();
        market.setSkill("Angular");
        market.setDemandCount(100);
        market.setFreelancerCount(10);
        market.setAvgBudget(BigDecimal.valueOf(1000));

        when(freelancerRepo.findById(1L)).thenReturn(Optional.of(freelancer));
        when(marketRepo.findAll()).thenReturn(List.of(market));

        SkillRecommendationRequestDTO request = new SkillRecommendationRequestDTO();
        request.setFreelancerId(1L);

        SkillRecommendationResponseDTO result = service.recommendSkills(request);

        assertFalse(result.getRecommendations().isEmpty());
        assertTrue(result.getCompatibilityGlobalPercent() >= 0);
    }

    @Test
    void shouldThrowException_whenNoFreelancer() {

        when(freelancerRepo.findById(1L)).thenReturn(Optional.empty());

        SkillRecommendationRequestDTO request = new SkillRecommendationRequestDTO();
        request.setFreelancerId(1L);

        assertThrows(RuntimeException.class, () -> {
            service.recommendSkills(request);
        });
    }
}