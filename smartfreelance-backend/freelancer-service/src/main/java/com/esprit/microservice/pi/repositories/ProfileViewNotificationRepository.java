package com.esprit.microservice.pi.repositories;

import com.esprit.microservice.pi.entites.ProfileViewNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileViewNotificationRepository extends JpaRepository<ProfileViewNotification, Long> {

    List<ProfileViewNotification> findByProfileOwnerIdOrderByViewedAtDesc(Long profileOwnerId);
}
