package com.esprit.microservice.pi.entites;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "profile_view_notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileViewNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long profileOwnerId;

    @Column(nullable = false)
    private Long viewerUserId;

    @Column(nullable = false, length = 120)
    private String viewerDisplayName;

    @Column(nullable = false)
    private LocalDateTime viewedAt;
}
