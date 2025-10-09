package br.com.hub.connect.application.gamification.badge.dto;

import jakarta.validation.constraints.Size;

public record UpdateBadgeDTO(
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,

        @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters") String description,

        @Size(max = 255, message = "Image URL must not exceed 255 characters") String imageUrl,

        @Size(min = 10, max = 500, message = "Criteria must be between 10 and 500 characters") String criteria) {
}