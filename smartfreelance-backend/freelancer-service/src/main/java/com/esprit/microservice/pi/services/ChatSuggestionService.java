package com.esprit.microservice.pi.services;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatSuggestionService {

    private final List<String> promptTemplates = Arrays.asList(
            "I'm looking for a developer %s",
            "need an expert on %s for my project",
            "What is the best freelancer for %s ?",
            "Recommend someone who is proficient %s",
            "I want to create an application with %s",
            "Looking for a senior profile in %s",
            "Developer fullstack %s and Spring Boot",
            "Expert %s available immediately"
    );

    private final List<String> knownSkills = Arrays.asList(
            "Angular", "React", "Vue", "Java", "Spring Boot", "Node.js", "Python", "SQL", "Docker", "AWS", "Flutter", "React Native",
            "C#", ".NET", "PHP", "Laravel", "Symfony", "JavaScript", "TypeScript", "HTML", "CSS", "Sass", "PostgreSQL", "MongoDB"
    );

    public List<String> getSuggestions(String query) {
        if (query == null || query.trim().length() < 2) {
            return Collections.emptyList();
        }

        String lowerQuery = query.toLowerCase().trim();
        List<String> suggestions = new ArrayList<>();

        // 1. Extraire les mots de la requête
        String[] words = lowerQuery.split("\\s+");

        // 2. Trouver les skills qui correspondent à n'importe quel mot de la requête
        // OU si un skill est contenu dans la requête
        List<String> matchingSkills = knownSkills.stream()
                .filter(skill -> {
                    String lowerSkill = skill.toLowerCase();
                    // Soit l'utilisateur tape le début d'un skill (ex: "Ang")
                    // Soit l'utilisateur tape une phrase contenant le skill (ex: "un dev angular")
                    return lowerSkill.contains(lowerQuery) || lowerQuery.contains(lowerSkill) ||
                           Arrays.stream(words).anyMatch(word -> word.length() >= 2 && lowerSkill.contains(word));
                })
                .limit(3)
                .collect(Collectors.toList());

        // 3. Si aucun skill ne matche, proposer des suggestions génériques
        if (matchingSkills.isEmpty()) {
            suggestions.add("Je cherche un freelancer...");
            suggestions.add("Quels sont les experts disponibles ?");
            suggestions.add("Besoin d'aide pour mon projet");
        } else {
            // 4. Générer des phrases basées sur les templates pour les skills trouvés
            for (String skill : matchingSkills) {
                // Alterner les templates pour plus de variété
                suggestions.add(String.format("Je cherche un développeur %s", skill));
                suggestions.add(String.format("Expert %s disponible ?", skill));
            }
        }

        // 5. Nettoyage et limitation
        return suggestions.stream()
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }
}
