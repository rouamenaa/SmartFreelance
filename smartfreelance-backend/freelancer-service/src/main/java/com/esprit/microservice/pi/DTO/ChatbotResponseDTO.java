package com.esprit.microservice.pi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponseDTO {
    private String name;
    private List<String> skills;
    private Double rating;
    private Double matchingScore;
}
