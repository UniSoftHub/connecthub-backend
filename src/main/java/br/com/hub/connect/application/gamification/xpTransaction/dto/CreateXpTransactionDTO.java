package br.com.hub.connect.application.gamification.xpTransaction.dto;

import br.com.hub.connect.domain.gamification.enums.XpType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateXpTransactionDTO(
        @NotNull(message = "User ID is required") Long userId,

        @NotNull(message = "Amount is required") @Min(value = 1, message = "Amount must be greater than 0") Integer amount,

        @NotBlank(message = "Description is required") @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters") String description,

        @NotNull(message = "Reference ID is required") Long referenceId,

        @NotNull(message = "Type is required") XpType type) {
}