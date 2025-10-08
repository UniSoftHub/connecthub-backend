package br.com.hub.connect.application.badge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBadgeDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
        String description,

        @NotBlank(message = "Image Url is required")
        @Size(max = 255, message = "Image URL must not exceed 255 characters")
        String imageUrl,

        @NotBlank(message = "Criteria is required")
        @Size(min = 10, max = 500, message = "Criteria must be between 10 and 500 characters")
        String criteria
) { }
