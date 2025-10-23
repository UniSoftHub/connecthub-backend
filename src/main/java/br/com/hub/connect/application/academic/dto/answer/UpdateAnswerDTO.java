package br.com.hub.connect.application.academic.dto.answer;

import jakarta.validation.constraints.Size;

public record UpdateAnswerDTO(
    @Size(min = 10, max = 5000, message = "Content must be between 10 and 5000 characters") String content,

    Boolean isSolution) {
}
