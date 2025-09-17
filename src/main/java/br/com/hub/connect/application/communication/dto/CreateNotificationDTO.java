package br.com.hub.connect.application.communication.dto;

import br.com.hub.connect.domain.communication.enums.NotificationType;
import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNotificationDTO(
    @NotBlank(message = "Title is required") @Size(min = 10, max = 100, message = "Title must be between 10 and 100 characters") String title,

    @NotBlank(message = "Message is required") @Size(min = 20, max = 200, message = "Message must be between 20 and 200 characters") String message,

    @NotNull Long referenceId,

    @NotNull Long userId,

    @NotNull NotificationType type) {

}
