package com.esprit.microservice.pi.services;

import com.esprit.microservice.pi.DTO.ProfileAnalyticsDTO;
import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.entites.PortfolioProject;
import com.esprit.microservice.pi.entites.Skill;
import com.esprit.microservice.pi.repositories.FreelancerRepository;
import com.esprit.microservice.pi.services.ProfileAnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileAnalyticsServiceTest {

    @Mock
    private FreelancerRepository repository;

    @InjectMocks
    private ProfileAnalyticsService service;

    @Test
    void shouldReturnAnalytics() {

        FreelancerProfile profile = new FreelancerProfile();
        profile.setSkills(List.of(new Skill(), new Skill()));
        profile.setProjects(List.of(new PortfolioProject()));
        profile.setHourlyRate(BigDecimal.valueOf(100));
        profile.setExperienceLevel("advanced");

        when(repository.findById(1L)).thenReturn(Optional.of(profile));

        ProfileAnalyticsDTO result = service.getAnalytics(1L);

        assertEquals(2, result.getTotalSkills());
        assertEquals(1, result.getTotalProjects());

    }

    @Test
    void shouldHandleNullValues() {

        FreelancerProfile profile = new FreelancerProfile();

        when(repository.findById(1L)).thenReturn(Optional.of(profile));

        ProfileAnalyticsDTO result = service.getAnalytics(1L);

        assertEquals(0, result.getTotalSkills());
        assertEquals(0, result.getTotalProjects());

    }
}