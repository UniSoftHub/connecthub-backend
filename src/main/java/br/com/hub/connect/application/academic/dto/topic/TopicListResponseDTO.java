package br.com.hub.connect.application.academic.dto.topic;

import java.util.List;

public record TopicListResponseDTO(int pages, List<TopicResponseDTO> topics) {
}
