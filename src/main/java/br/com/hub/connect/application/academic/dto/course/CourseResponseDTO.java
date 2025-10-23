package br.com.hub.connect.application.academic.dto.course;

import java.time.LocalDateTime;

public record CourseResponseDTO(Long id,
    String name,
    String code,
    Integer semester,
    Integer workload,
    LocalDateTime createdAt) {
}
