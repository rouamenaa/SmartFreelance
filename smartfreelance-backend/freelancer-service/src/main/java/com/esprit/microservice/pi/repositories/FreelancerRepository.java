package com.esprit.microservice.pi.repositories;

import com.esprit.microservice.pi.entites.FreelancerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerRepository
        extends JpaRepository<FreelancerProfile, Long> {
}