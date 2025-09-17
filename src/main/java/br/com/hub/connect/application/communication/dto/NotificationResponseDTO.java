package br.com.hub.connect.application.communication.dto;

import br.com.hub.connect.domain.communication.enums.NotificationType;

public record NotificationResponseDTO(
    Long id,
    NotificationType type,
    String title,
    String message,
    Long referenceId,
    Long userId) {
}
