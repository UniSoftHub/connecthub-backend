package br.com.hub.connect.application.gamification.xpTransaction.dto;

import br.com.hub.connect.domain.gamification.enums.XpType;
import java.time.LocalDateTime;

public record ResponseXpTransactionDTO(
    Long id,
    Long userId,
    String userName,
    Integer amount,
    String description,
    Long referenceId,
    XpType type,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
