package com.esprit.microservice.pi.services;

import com.esprit.microservice.pi.DTO.ProfileViewNotificationDTO;
import com.esprit.microservice.pi.entites.ProfileViewNotification;
import com.esprit.microservice.pi.repositories.FreelancerRepository;
import com.esprit.microservice.pi.repositories.ProfileViewNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileViewNotificationService {

    private final ProfileViewNotificationRepository notificationRepo;
    private final FreelancerRepository freelancerRepo;

    public ProfileViewNotificationService(ProfileViewNotificationRepository notificationRepo,
                                          FreelancerRepository freelancerRepo) {
        this.notificationRepo = notificationRepo;
        this.freelancerRepo = freelancerRepo;
    }

    @Transactional
    public Optional<ProfileViewNotificationDTO> recordProfileView(Long profileOwnerId, Long viewerUserId) {
        if (viewerUserId == null || profileOwnerId == null) {
            return Optional.empty();
        }
        if (profileOwnerId.equals(viewerUserId)) {
            return Optional.empty();
        }
        if (!freelancerRepo.existsById(profileOwnerId)) {
            throw new RuntimeException("Profile not found");
        }

        String viewerName = freelancerRepo.findById(viewerUserId)
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("User #" + viewerUserId);

        LocalDateTime now = LocalDateTime.now();
        ProfileViewNotification entity = ProfileViewNotification.builder()
                .profileOwnerId(profileOwnerId)
                .viewerUserId(viewerUserId)
                .viewerDisplayName(viewerName)
                .viewedAt(now)
                .build();

        entity = notificationRepo.save(entity);
        return Optional.of(toDto(entity));
    }

    @Transactional(readOnly = true)
    public List<ProfileViewNotificationDTO> listForProfileOwner(Long profileOwnerId) {
        return notificationRepo.findByProfileOwnerIdOrderByViewedAtDesc(profileOwnerId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ProfileViewNotificationDTO toDto(ProfileViewNotification n) {
        String message = n.getViewerDisplayName() + " viewed your profile";
        return ProfileViewNotificationDTO.builder()
                .id(n.getId())
                .profileOwnerId(n.getProfileOwnerId())
                .viewerUserId(n.getViewerUserId())
                .viewerDisplayName(n.getViewerDisplayName())
                .message(message)
                .viewedAt(n.getViewedAt())
                .build();
    }
}
