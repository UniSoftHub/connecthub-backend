package br.com.hub.connect.application.project.projectComment.dto;

import java.time.LocalDateTime;

public record ProjectCommentResponseDTO(
    Long id,
    String text,
    Long authorId,
    LocalDateTime createdAt,
    Long projectId) {
}
