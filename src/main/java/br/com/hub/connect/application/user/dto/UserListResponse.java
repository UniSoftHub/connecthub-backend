package br.com.hub.connect.application.user.dto;

import java.util.List;

public record UserListResponse(int pages, List<UserResponseDTO> users) {
}
