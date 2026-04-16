package com.esprit.microservice.pi.services;

import com.esprit.microservice.pi.DTO.ProfileCompletionDTO;
import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.entites.PortfolioProject;
import com.esprit.microservice.pi.entites.Skill;
import com.esprit.microservice.pi.repositories.FreelancerRepository;
import com.esprit.microservice.pi.services.ProfileCompletionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileCompletionServiceTest {

    @Mock
    private FreelancerRepository repository;

    @InjectMocks
    private ProfileCompletionService service;

    @Test
    void shouldCalculateCompletion_fullProfile() {

        FreelancerProfile profile = new FreelancerProfile();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setTitle("Dev");
        profile.setOverview("Good dev");
        profile.setHourlyRate(BigDecimal.valueOf(50));
        profile.setExperienceLevel("expert");
        profile.setAvailability("full-time");
        profile.setCountry("France");
        profile.setSkills(List.of(new Skill()));
        profile.setProjects(List.of(new PortfolioProject()));

        when(repository.findById(1L)).thenReturn(Optional.of(profile));

        ProfileCompletionDTO result = service.calculateCompletion(1L);

        assertEquals(100, result.getPercentage());
        assertTrue(result.getMissingFields().isEmpty());
    }
}