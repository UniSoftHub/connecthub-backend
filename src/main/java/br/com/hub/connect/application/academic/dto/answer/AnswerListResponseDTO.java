package br.com.hub.connect.application.academic.dto.answer;

import java.util.List;

public record AnswerListResponseDTO(int pages, List<AnswerResponseDTO> answers) {
}
