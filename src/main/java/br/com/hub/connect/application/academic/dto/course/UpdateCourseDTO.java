package br.com.hub.connect.application.academic.dto.course;

import jakarta.validation.constraints.Size;

public record UpdateCourseDTO(
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters") String code, Integer semester,
    Integer workload) {
}
