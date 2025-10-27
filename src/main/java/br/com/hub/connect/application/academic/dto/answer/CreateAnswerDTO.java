package br.com.hub.connect.application.academic.dto.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAnswerDTO(
    @NotBlank(message = "Content is required") @Size(min = 10, max = 5000, message = "Content must be between 10 and 5000 characters") String content,

    @NotNull(message = "Topic ID is required") Long topicId,

    @NotNull(message = "Author ID is required") Long authorId) {
}
