package com.esprit.microservice.pi.services;

import com.esprit.microservice.pi.DTO.ChatbotResponseDTO;
import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.entites.Skill;
import com.esprit.microservice.pi.repositories.FreelancerRepository;
import com.esprit.microservice.pi.repositories.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final FreelancerRepository freelancerRepository;
    private final SkillRepository skillRepository;

    // Liste de compétences connues pour l'extraction (peut être enrichie dynamiquement)
    private final List<String> knownSkills = Arrays.asList(
            "Angular", "React", "Vue", "Java", "Spring Boot", "Node.js", "Python", "SQL", "Docker", "AWS", "Flutter", "React Native",
            "C#", ".NET", "PHP", "Laravel", "Symfony", "JavaScript", "TypeScript", "HTML", "CSS", "Sass", "PostgreSQL", "MongoDB",
            "Redis", "Kubernetes", "Azure", "GCP", "DevOps", "CI/CD", "JUnit", "Selenium", "Swift", "Kotlin", "Android", "iOS"
    );

    public List<ChatbotResponseDTO> recommendFreelancers(String userInput) {
        // 1. Extraire les skills du texte
        List<String> requestedSkills = extractSkills(userInput);

        if (requestedSkills.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Récupérer tous les freelancers
        List<FreelancerProfile> freelancers = freelancerRepository.findAll();

        // 3. Calculer le score et mapper vers DTO
        List<ChatbotResponseDTO> recommendations = freelancers.stream()
                .map(f -> {
                    List<String> freelancerSkills = f.getSkills() != null 
                            ? f.getSkills().stream().map(Skill::getName).collect(Collectors.toList())
                            : Collections.emptyList();

                    long commonSkillsCount = requestedSkills.stream()
                            .filter(s -> freelancerSkills.stream()
                                    .anyMatch(fs -> fs.equalsIgnoreCase(s)))
                            .count();

                    double matchingScore = (double) commonSkillsCount / requestedSkills.size() * 100;

                    return new ChatbotResponseDTO(
                            f.getFirstName() + " " + f.getLastName(),
                            freelancerSkills,
                            f.getRating() != null ? f.getRating() : 0.0,
                            matchingScore
                    );
                })
                .filter(dto -> dto.getMatchingScore() > 0)
                .sorted(Comparator.comparing(ChatbotResponseDTO::getMatchingScore).reversed())
                .limit(3)
                .collect(Collectors.toList());

        return recommendations;
    }

    private List<String> extractSkills(String text) {
        String normalizedText = normalize(text);
        return knownSkills.stream()
                .filter(skill -> normalizedText.contains(" " + normalize(skill).trim() + " "))
                .collect(Collectors.toList());
    }

    private String normalize(String input) {
        if (input == null) return "";
        return " " + input.toLowerCase().replaceAll("[^a-zA-Z0-9#+]", " ").replaceAll("\\s+", " ") + " ";
    }
}
