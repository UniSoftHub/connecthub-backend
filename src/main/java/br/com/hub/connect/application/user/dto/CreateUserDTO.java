package br.com.hub.connect.application.user.dto;

import br.com.hub.connect.domain.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
    @NotBlank(message = "Name is required") @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,

    @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,

    @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,

    UserRole role)

{
}
