package com.esprit.microservice.pi.services;

import com.esprit.microservice.pi.DTO.SkillOpportunityDTO;
import com.esprit.microservice.pi.DTO.SkillRecommendationRequestDTO;
import com.esprit.microservice.pi.DTO.SkillRecommendationResponseDTO;
import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.entites.Skill;
import com.esprit.microservice.pi.entites.SkillMarket;
import com.esprit.microservice.pi.repositories.FreelancerRepository;
import com.esprit.microservice.pi.repositories.SkillMarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillMarketRecommendationService {

    private final SkillMarketRepository skillMarketRepository;
    private final FreelancerRepository freelancerRepository;

    public SkillRecommendationResponseDTO recommendSkills(SkillRecommendationRequestDTO request) {
        if (request.getFreelancerId() == null) {
            throw new RuntimeException("freelancerId is required");
        }

        FreelancerProfile freelancer = freelancerRepository.findById(request.getFreelancerId())
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        int maxResults = request.getMaxResults() == null ? 3 : Math.max(1, request.getMaxResults());
        Set<String> freelancerSkills = normalizeFreelancerSkills(freelancer.getSkills());

        List<SkillMarket> marketSkills = skillMarketRepository.findAll();
        if (marketSkills.isEmpty()) {
            return new SkillRecommendationResponseDTO(
                    "Aucune donnee de marche disponible pour l'instant.",
                    0,
                    List.of()
            );
        }

        BigDecimal maxOpportunity = marketSkills.stream()
                .map(this::calculateOpportunityScore)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ONE);

        long demandedSkillsKnown = marketSkills.stream()
                .map(SkillMarket::getSkill)
                .map(this::normalize)
                .filter(freelancerSkills::contains)
                .count();

        int compatibilityGlobal = (int) Math.round((demandedSkillsKnown * 100.0) / marketSkills.size());

        List<SkillOpportunityDTO> recommendations = marketSkills.stream()
                .filter(entry -> !freelancerSkills.contains(normalize(entry.getSkill())))
                .map(entry -> toOpportunityDto(entry, maxOpportunity, compatibilityGlobal))
                .sorted(Comparator.comparing(SkillOpportunityDTO::getOpportunityScore).reversed())
                .limit(maxResults)
                .toList();

        if (recommendations.isEmpty()) {
            return new SkillRecommendationResponseDTO(
                    "Excellent profil : vous possedez deja toutes les competences suivies par le marche.",
                    100,
                    List.of()
            );
        }

        SkillOpportunityDTO best = recommendations.get(0);
        String summary = "Tu devrais apprendre " + best.getSkill()
                + " pour augmenter tes opportunites de +" + best.getOpportunityBoostPercent() + "%";

        return new SkillRecommendationResponseDTO(summary, compatibilityGlobal, recommendations);
    }

    private SkillOpportunityDTO toOpportunityDto(SkillMarket entry, BigDecimal maxOpportunity, int compatibilityGlobal) {
        BigDecimal opportunityScore = calculateOpportunityScore(entry);
        int opportunityBoost = toPercentage(opportunityScore, maxOpportunity);
        int compatibilityPercent = Math.min(100, (int) Math.round((compatibilityGlobal * 0.6) + (opportunityBoost * 0.4)));

        String aiMessage = "Tu devrais apprendre " + entry.getSkill()
                + " pour augmenter tes opportunites de +" + opportunityBoost + "%";

        return new SkillOpportunityDTO(
                entry.getSkill(),
                entry.getDemandCount(),
                entry.getFreelancerCount(),
                entry.getAvgBudget(),
                opportunityScore,
                compatibilityPercent,
                opportunityBoost,
                aiMessage
        );
    }

    private BigDecimal calculateOpportunityScore(SkillMarket market) {
        int demandGap = market.getDemandCount() - market.getFreelancerCount();
        int positiveGap = Math.max(demandGap, 0);
        return BigDecimal.valueOf(positiveGap)
                .multiply(market.getAvgBudget())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private int toPercentage(BigDecimal value, BigDecimal max) {
        if (max == null || max.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        return value.multiply(BigDecimal.valueOf(100))
                .divide(max, 0, RoundingMode.HALF_UP)
                .intValue();
    }

    private Set<String> normalizeFreelancerSkills(List<Skill> skills) {
        if (skills == null) {
            return Set.of();
        }
        return skills.stream()
                .map(Skill::getName)
                .map(this::normalize)
                .collect(Collectors.toSet());
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
