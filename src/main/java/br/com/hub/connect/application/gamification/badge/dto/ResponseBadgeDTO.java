package br.com.hub.connect.application.gamification.badge.dto;

import java.time.LocalDateTime;

public record ResponseBadgeDTO(
    Long id,
    String name,
    String description,
    String imageUrl,
    String criteria,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
