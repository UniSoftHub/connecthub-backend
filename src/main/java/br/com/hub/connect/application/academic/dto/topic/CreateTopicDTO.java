package br.com.hub.connect.application.academic.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTopicDTO(
    @NotBlank(message = "Title is required") @Size(min = 2, max = 150, message = "Title must be between 2 and 150 characters") String title,

    @NotBlank(message = "Content is required") @Size(min = 10, max = 5000, message = "Content must be between 10 and 5000 characters") String content,

    @NotNull(message = "Course ID is required") Long courseId,

    @NotNull(message = "Author ID is required") Long authorId) {
}
