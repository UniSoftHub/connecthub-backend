package br.com.hub.connect.application.userbadge.dto;

import java.time.LocalDateTime;

public record ResponseUserBadgeDTO(
    Long id,
    Long userId,
    String userName,
    Long badgeId,
    String badgeName,
    String badgeDescription,
    String badgeImageUrl,
    LocalDateTime earnedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) { }