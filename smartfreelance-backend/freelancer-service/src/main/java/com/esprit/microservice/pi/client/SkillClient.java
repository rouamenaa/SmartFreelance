package com.esprit.microservice.pi.client;

import com.esprit.microservice.pi.dto.SkillDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "skill-service",
        path = "/api/skills"
)
public interface SkillClient {

    /**
     * Get all skills of a freelancer
     */
    @GetMapping("/freelancer/{freelancerId}")
    List<SkillDTO> getSkillsByFreelancerId(@PathVariable("freelancerId") Long freelancerId);


    /**
     * Get skill details by ID
     */
    @GetMapping("/{id}")
    SkillDTO getSkillDetails(@PathVariable("id") Long id);
}