package br.com.hub.connect.application.userbadge.dto;

import jakarta.validation.constraints.NotNull;

public record CreateUserBadgeDTO(
    @NotNull(message = "User ID is required")
    Long userId,
    
    @NotNull(message = "Badge ID is required")
    Long badgeId
) { }