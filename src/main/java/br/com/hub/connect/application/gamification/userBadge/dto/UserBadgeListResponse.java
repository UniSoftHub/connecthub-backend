package br.com.hub.connect.application.gamification.userBadge.dto;

import java.util.List;

public record UserBadgeListResponse(int pages, List<ResponseUserBadgeDTO> userBadges) {
}