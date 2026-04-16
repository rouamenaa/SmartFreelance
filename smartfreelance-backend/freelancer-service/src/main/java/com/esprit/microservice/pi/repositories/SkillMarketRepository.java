package com.esprit.microservice.pi.repositories;

import com.esprit.microservice.pi.entites.SkillMarket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillMarketRepository extends JpaRepository<SkillMarket, Long> {
    Optional<SkillMarket> findBySkillIgnoreCase(String skill);
}
