package br.com.hub.connect.application.project.projectComment.dto;

import java.util.List;

public record ProjectCommentListResponseDTO(int pages, List<ProjectCommentResponseDTO> comments) {
}