package br.com.hub.connect.application.academic.dto.topic;

import java.time.LocalDateTime;
import br.com.hub.connect.domain.academic.enums.TopicStatus;

public record TopicResponseDTO(
    Long id,
    String title,
    String content,
    Long courseId,
    String courseName,
    Long authorId,
    String authorName,
    TopicStatus status,
    Integer countViews,
    LocalDateTime createdAt) {
}
