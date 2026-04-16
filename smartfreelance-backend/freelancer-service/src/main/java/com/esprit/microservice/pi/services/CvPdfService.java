package com.esprit.microservice.pi.services;

import com.esprit.microservice.pi.entites.FreelancerProfile;
import com.esprit.microservice.pi.entites.PortfolioProject;
import com.esprit.microservice.pi.entites.Skill;
import com.esprit.microservice.pi.repositories.FreelancerRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CvPdfService {

    private final FreelancerRepository freelancerRepository;

    public byte[] generateCvPdf(Long userId) {
        FreelancerProfile profile = freelancerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            addHeader(document, profile);
            addProfileSection(document, profile);
            addSkillsSection(document, profile.getSkills());
            addProjectsSection(document, profile.getProjects());

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CV PDF", e);
        }
    }

    private void addHeader(Document document, FreelancerProfile profile) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph(profile.getFirstName() + " " + profile.getLastName(), titleFont));
        document.add(new Paragraph(safe(profile.getTitle()), subtitleFont));
        document.add(new Paragraph(" "));
    }

    private void addProfileSection(Document document, FreelancerProfile profile) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        document.add(new Paragraph("Profile", sectionFont));
        document.add(new Paragraph("Overview: " + safe(profile.getOverview()), textFont));
        document.add(new Paragraph("Country: " + safe(profile.getCountry()), textFont));
        document.add(new Paragraph("Availability: " + safe(profile.getAvailability()), textFont));
        document.add(new Paragraph("Experience Level: " + safe(profile.getExperienceLevel()), textFont));
        document.add(new Paragraph("Hourly Rate: $" + (profile.getHourlyRate() == null ? "N/A" : profile.getHourlyRate()), textFont));
        document.add(new Paragraph(" "));
    }

    private void addSkillsSection(Document document, List<Skill> skills) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        document.add(new Paragraph("Skills", sectionFont));
        if (skills == null || skills.isEmpty()) {
            document.add(new Paragraph("- No skills added yet.", textFont));
        } else {
            for (Skill skill : skills) {
                document.add(new Paragraph("- " + safe(skill.getName()) + " (" + safe(skill.getLevel()) + ")", textFont));
            }
        }
        document.add(new Paragraph(" "));
    }

    private void addProjectsSection(Document document, List<PortfolioProject> projects) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        document.add(new Paragraph("Projects", sectionFont));
        if (projects == null || projects.isEmpty()) {
            document.add(new Paragraph("- No projects added yet.", textFont));
            return;
        }

        for (PortfolioProject project : projects) {
            document.add(new Paragraph("- " + safe(project.getTitle()), textFont));
            document.add(new Paragraph("  Description: " + safe(project.getDescription()), textFont));
            document.add(new Paragraph("  Technologies: " + safe(project.getTechnologiesUsed()), textFont));
            document.add(new Paragraph("  URL: " + safe(project.getProjectUrl()), textFont));
            document.add(new Paragraph(" "));
        }
    }

    private String safe(String value) {
        return (value == null || value.isBlank()) ? "N/A" : value;
    }
}
