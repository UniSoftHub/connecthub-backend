package br.com.hub.connect.application.academic.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCourseDTO(
    @NotBlank(message = "Name is required") @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,
    @NotBlank(message = "Code is required") @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters") String code,
    @NotNull(message = "Semester is required") Integer semester, Integer workload) {
}
