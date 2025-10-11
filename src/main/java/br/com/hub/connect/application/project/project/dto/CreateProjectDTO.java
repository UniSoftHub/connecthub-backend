package br.com.hub.connect.application.project.project.dto;

import br.com.hub.connect.domain.project.enums.ProjectTechnologies;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProjectDTO(
    @NotBlank(message = "Name is required") @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,

    @NotBlank(message = "Description is required") @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters") String description,

    @NotNull(message = "Owner ID is required") @Min(value = 1, message = "Owner ID must be a positive number") Long authorId,

    @NotBlank(message = "Repository URL is required") @Size(min = 10, max = 200, message = "Repository URL must be between 10 and 200 characters") String repositoryUrl,

    @Size(max = 200, message = "Image URL must be up to 200 characters") String imageUrl,

    @NotNull(message = "Technologies are required") ProjectTechnologies technologies

) {

}
