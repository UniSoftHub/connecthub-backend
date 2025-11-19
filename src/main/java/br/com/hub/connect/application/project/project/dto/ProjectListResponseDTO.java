package br.com.hub.connect.application.project.project.dto;

import java.util.List;

public record ProjectListResponseDTO(int pages,List<ProjectResponseDTO> projects) {
}