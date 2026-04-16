package com.esprit.microservice.pi.controlleurs;

import com.esprit.microservice.pi.DTO.ProfileAnalyticsDTO;
import com.esprit.microservice.pi.DTO.ProfileCompletionDTO;
import com.esprit.microservice.pi.DTO.ProfileViewNotificationDTO;
import com.esprit.microservice.pi.DTO.RecordProfileViewRequestDTO;
import com.esprit.microservice.pi.DTO.SkillRecommendationDTO;
import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.entites.PortfolioProject;
import com.esprit.microservice.pi.entites.Skill;
import com.esprit.microservice.pi.services.CvPdfService;
import com.esprit.microservice.pi.services.FreelancerService;
import com.esprit.microservice.pi.services.ProfileViewNotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class FreelancerController {

    private final FreelancerService service;
    private final CvPdfService cvPdfService;
    private final ProfileViewNotificationService profileViewNotificationService;

    public FreelancerController(FreelancerService service,
                                CvPdfService cvPdfService,
                                ProfileViewNotificationService profileViewNotificationService) {
        this.service = service;
        this.cvPdfService = cvPdfService;
        this.profileViewNotificationService = profileViewNotificationService;
    }

    // ─────────────────────────────────────────────
    // 🔹 PROFILE
    // ─────────────────────────────────────────────

    @GetMapping("/{userId}")
    public FreelancerProfile getProfile(@PathVariable Long userId) {
        return service.getProfile(userId);
    }

    @PutMapping("/{userId}")
    public FreelancerProfile updateProfile(@PathVariable Long userId,
                                           @RequestBody FreelancerProfile profile) {
        return service.updateProfile(userId, profile);
    }

    // ─────────────────────────────────────────────
    // 🔹 SKILLS
    // ─────────────────────────────────────────────

    @PostMapping("/{userId}/skills")
    public Skill addSkill(@PathVariable Long userId,
                          @RequestBody Skill skill) {
        return service.addSkill(userId, skill);
    }

    @GetMapping("/{userId}/skills")
    public List<Skill> getSkills(@PathVariable Long userId) {
        return service.getSkills(userId);
    }

    @PutMapping("/skills/{skillId}")
    public Skill updateSkill(@PathVariable Long skillId,
                             @RequestBody Skill skill) {
        return service.updateSkill(skillId, skill);
    }

    @DeleteMapping("/skills/{skillId}")
    public void deleteSkill(@PathVariable Long skillId) {
        service.deleteSkill(skillId);
    }

    // ─────────────────────────────────────────────
    // 🔹 PROJECTS
    // ─────────────────────────────────────────────

    @PostMapping("/{userId}/projects")
    public PortfolioProject addProject(@PathVariable Long userId,
                                       @Valid @RequestBody PortfolioProject project) {
        return service.addProject(userId, project);
    }

    @GetMapping("/{userId}/projects")
    public List<PortfolioProject> getProjects(@PathVariable Long userId) {
        return service.getProjects(userId);
    }

    @PutMapping("/projects/{projectId}")
    public PortfolioProject updateProject(@PathVariable Long projectId,
                                          @Valid @RequestBody PortfolioProject project) {
        return service.updateProject(projectId, project);
    }

    @DeleteMapping("/projects/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        service.deleteProject(projectId);
    }

    // ─────────────────────────────────────────────
    // 🔹 ADVANCED FEATURE 1 — Profile Analytics
    // GET /api/profile/{userId}/analytics
    // ─────────────────────────────────────────────

    /**
     * Returns: totalSkills, totalProjects, hourlyRate, experienceInYears
     * Example response:
     * {
     *   "totalSkills": 5,
     *   "totalProjects": 3,
     *   "hourlyRate": 45.00,
     *   "experienceInYears": 6
     * }
     */
    @GetMapping("/{userId}/analytics")
    public ProfileAnalyticsDTO getAnalytics(@PathVariable Long userId) {
        return service.getProfileAnalytics(userId);
    }

    // ─────────────────────────────────────────────
    // 🔹 ADVANCED FEATURE 2 — Profile Completion
    // GET /api/profile/{userId}/completion
    // ─────────────────────────────────────────────

    /**
     * Returns the completion percentage and list of missing fields.
     * Example response:
     * {
     *   "percentage": 70,
     *   "missingFields": ["overview", "projects (add at least 1)"]
     * }
     */
    @GetMapping("/{userId}/completion")
    public ProfileCompletionDTO getCompletion(@PathVariable Long userId) {
        return service.getProfileCompletion(userId);
    }

    // ─────────────────────────────────────────────
    // 🔹 ADVANCED FEATURE 3 — Skill Recommendation
    // GET /api/profile/{userId}/skill-recommendation
    // ─────────────────────────────────────────────

    /**
     * Analyses current skills and returns dominant skill, top 3 skills, global score.
     * Example response:
     * {
     *   "dominantSkill": "Java",
     *   "topSkills": ["Java", "Spring Boot", "Docker"],
     *   "globalSkillScore": 75
     * }
     */
    @GetMapping("/{userId}/skill-recommendation")
    public SkillRecommendationDTO getSkillRecommendation(@PathVariable Long userId) {
        return service.getSkillRecommendation(userId);
    }

    @GetMapping("/{userId}/cv")
    public ResponseEntity<byte[]> downloadCv(@PathVariable Long userId) {
        byte[] pdfBytes = cvPdfService.generateCvPdf(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=freelancer-cv-" + userId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // ─────────────────────────────────────────────
    // Profile view notifications (LinkedIn-style)
    // ─────────────────────────────────────────────

    /**
     * Call when a user opens someone else's freelancer profile.
     * No notification is created when the viewer is the profile owner.
     */
    @PostMapping("/{profileOwnerId}/views")
    public ResponseEntity<ProfileViewNotificationDTO> recordProfileView(
            @PathVariable Long profileOwnerId,
            @Valid @RequestBody RecordProfileViewRequestDTO body) {
        return profileViewNotificationService.recordProfileView(profileOwnerId, body.getViewerUserId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /**
     * Notifications for the profile owner (recent profile views).
     */
    @GetMapping("/{userId}/view-notifications")
    public List<ProfileViewNotificationDTO> getViewNotifications(@PathVariable Long userId) {
        return profileViewNotificationService.listForProfileOwner(userId);
    }
}