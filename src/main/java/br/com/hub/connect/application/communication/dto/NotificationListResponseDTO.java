package br.com.hub.connect.application.communication.dto;

import java.util.List;

public record NotificationListResponseDTO(int pages, List<NotificationResponseDTO> notifications) {
}
