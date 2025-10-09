package br.com.hub.connect.application.gamification.xpTransaction.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateXpTransactionDTO(
        @Min(value = 1, message = "Amount must be greater than 0") Integer amount,

        @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters") String description) {
}
