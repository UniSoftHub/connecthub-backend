package br.com.hub.connect.application.project.project.dto;

import java.time.LocalDateTime;
import java.util.Set;
import br.com.hub.connect.domain.project.enums.ProjectTechnologies;
import br.com.hub.connect.application.user.dto.UserResponseDTO;

public record ProjectResponseDTO(
        Long id,
        String name,
        String description,
        String repositoryUrl,
        String imageUrl,
        Set<ProjectTechnologies> technologies,
        int countViews,
        LocalDateTime createdAt,
        UserResponseDTO author) {
}
