package com.esprit.microservice.pi.services;

import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.repositories.FreelancerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreelancerServiceTest {

    @Mock
    private FreelancerRepository freelancerRepo;

    @InjectMocks
    private FreelancerService service;

    @Test
    void shouldGetProfile() {

        FreelancerProfile profile = new FreelancerProfile();
        profile.setId(1L);

        when(freelancerRepo.findById(1L)).thenReturn(Optional.of(profile));

        FreelancerProfile result = service.getProfile(1L);

        assertEquals(1L, result.getId());
    }
}