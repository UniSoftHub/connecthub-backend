package br.com.hub.connect.application.user.dto;

import java.util.List;

public record UserListResponseDTO(int pages, List<UserResponseDTO> users) {
}
