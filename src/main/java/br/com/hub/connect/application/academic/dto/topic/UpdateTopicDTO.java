package br.com.hub.connect.application.academic.dto.topic;

import jakarta.validation.constraints.Size;
import br.com.hub.connect.domain.academic.enums.TopicStatus;

public record UpdateTopicDTO(
    @Size(min = 2, max = 150, message = "Title must be between 2 and 150 characters") String title,

    @Size(min = 10, max = 5000, message = "Content must be between 10 and 5000 characters") String content,

    TopicStatus status) {
}
