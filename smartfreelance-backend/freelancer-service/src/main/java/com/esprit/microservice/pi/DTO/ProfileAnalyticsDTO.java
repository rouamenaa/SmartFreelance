package com.esprit.microservice.pi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProfileAnalyticsDTO {
    private int totalSkills;
    private int totalProjects;
    private BigDecimal hourlyRate;
    private int experienceInYears; // ← add this
}