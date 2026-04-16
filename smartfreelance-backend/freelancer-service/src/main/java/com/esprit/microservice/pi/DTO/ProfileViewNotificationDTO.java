package com.esprit.microservice.pi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileViewNotificationDTO {

    private Long id;
    private Long profileOwnerId;
    private Long viewerUserId;
    private String viewerDisplayName;
    private String message;
    private LocalDateTime viewedAt;
}
