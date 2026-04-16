package com.esprit.microservice.pi.DTO;

import lombok.Data;

@Data
public class SkillRecommendationRequestDTO {
    private Long freelancerId;
    private Integer maxResults = 3;
}
