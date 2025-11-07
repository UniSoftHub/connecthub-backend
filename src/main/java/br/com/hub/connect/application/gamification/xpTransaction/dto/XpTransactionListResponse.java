package br.com.hub.connect.application.gamification.xpTransaction.dto;

import java.util.List;

public record XpTransactionListResponse(int pages, List<ResponseXpTransactionDTO> xpTransactions) {
}