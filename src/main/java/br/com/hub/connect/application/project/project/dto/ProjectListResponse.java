package br.com.hub.connect.application.project.project.dto;

import java.util.List;

public record ProjectListResponse(int pages,List<ProjectResponseDTO> projects) {
}