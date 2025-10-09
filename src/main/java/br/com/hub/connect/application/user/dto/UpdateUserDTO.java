package br.com.hub.connect.application.user.dto;

import br.com.hub.connect.domain.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,

        @Email(message = "Email must be valid") String email,

        @Size(min = 6, message = "Password must be at least 6 characters") String password,

        UserRole role) {
}
