package com.esprit.microservice.pi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SkillRecommendationResponseDTO {
    private String summaryMessage;
    private int compatibilityGlobalPercent;
    private List<SkillOpportunityDTO> recommendations;
}
