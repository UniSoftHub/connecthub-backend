package br.com.hub.connect.application.project.project.dto;

import br.com.hub.connect.domain.project.enums.ProjectTechnologies;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateProjectDTO(
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,

    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters") String description,

    @Min(value = 1, message = "Author ID must be a positive number") Long authorId,

    @Size(min = 10, max = 200, message = "Repository URL must be between 10 and 200 characters") String repositoryUrl,

    @Size(max = 200, message = "Image URL must be up to 200 characters") String imageUrl,

    ProjectTechnologies technologies,

    @Min(value = 0, message = "Count Views must be a positive number") Integer countViews
) {

}
