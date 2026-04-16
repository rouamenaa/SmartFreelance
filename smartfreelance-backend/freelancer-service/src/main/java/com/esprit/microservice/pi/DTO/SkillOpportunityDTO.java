package com.esprit.microservice.pi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SkillOpportunityDTO {
    private String skill;
    private int demandCount;
    private int freelancerCount;
    private BigDecimal avgBudget;
    private BigDecimal opportunityScore;
    private int compatibilityPercent;
    private int opportunityBoostPercent;
    private String aiMessage;
}
