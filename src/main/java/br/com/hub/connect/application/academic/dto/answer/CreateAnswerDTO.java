package br.com.hub.connect.application.academic.dto.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAnswerDTO(
    @NotBlank(message = "Content is required") String content,

    @NotNull(message = "Topic ID is required") Long topicId) {
}
