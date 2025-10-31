package br.com.hub.connect.application.academic.dto.course;

import java.util.List;

public record CourseListResponseDTO(int pages, List<CourseResponseDTO> courses) {
}
