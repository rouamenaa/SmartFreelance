package com.esprit.microservice.pi.controlleurs;

import com.esprit.microservice.pi.DTO.SkillRecommendationRequestDTO;
import com.esprit.microservice.pi.DTO.SkillRecommendationResponseDTO;
import com.esprit.microservice.pi.services.SkillMarketRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillMarketRecommendationController {

    private final SkillMarketRecommendationService recommendationService;

    @PostMapping("/recommend")
    public SkillRecommendationResponseDTO recommend(@RequestBody SkillRecommendationRequestDTO request) {
        return recommendationService.recommendSkills(request);
    }
}
