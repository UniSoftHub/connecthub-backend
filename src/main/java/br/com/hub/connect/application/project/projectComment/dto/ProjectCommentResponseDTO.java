package br.com.hub.connect.application.project.projectComment.dto;

import java.time.LocalDateTime;

import br.com.hub.connect.application.user.dto.UserResponseDTO;

public record ProjectCommentResponseDTO(
    Long id,
    String text,
    UserResponseDTO author,
    LocalDateTime createdAt,
    Long projectId) {
}
