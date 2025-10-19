package br.com.hub.connect.application.academic.dto.answer;

import java.time.LocalDateTime;

public record AnswerResponseDTO(
    Long id,
    String content,
    Long topicId,
    String topicTitle,
    Long authorId,
    String authorName,
    Integer countLikes,
    Integer countDislikes,
    Boolean isSolution,
    LocalDateTime createdAt) {
}
