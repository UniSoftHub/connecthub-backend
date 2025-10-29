package br.com.hub.connect.application.project.projectComment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProjectCommentDTO(
        @NotBlank(message = "Text is required") @Size(min = 1, max = 500, message = "Text must be between 1 and 500 characters") String text,

        @NotNull(message = "Author ID is required") @Min(value = 1, message = "Author ID must be a positive number") Long authorId) {

}
