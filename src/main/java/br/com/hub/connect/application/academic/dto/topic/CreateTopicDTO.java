package br.com.hub.connect.application.academic.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTopicDTO(
    @NotBlank(message = "Title is required") String title,

    @NotBlank(message = "Content is required") String content,

    @NotNull(message = "Course ID is required") Long courseId) {
}
