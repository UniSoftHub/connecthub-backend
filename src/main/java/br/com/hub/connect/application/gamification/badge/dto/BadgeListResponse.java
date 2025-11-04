package br.com.hub.connect.application.gamification.badge.dto;

import java.util.List;

public record BadgeListResponse(int pages, List<ResponseBadgeDTO> badges) {
}