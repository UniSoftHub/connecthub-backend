package br.com.hub.connect.application.user.service;

import java.util.List;
import java.util.stream.Collectors;

import br.com.hub.connect.application.user.dto.CreateUserDTO;
import br.com.hub.connect.application.user.dto.UpdateUserDTO;
import br.com.hub.connect.application.user.dto.UserResponseDTO;
import br.com.hub.connect.domain.exception.EmailAlreadyExistsException;
import br.com.hub.connect.domain.exception.UserNotFoundException;
import br.com.hub.connect.domain.user.enums.UserRole;
import br.com.hub.connect.domain.user.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class UserService {

  public List<UserResponseDTO> findAll(int page, int size) {
    return User.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public UserResponseDTO findById(@NotNull Long id) {
    User user = User.findActiveById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

    return toResponseDTO(user);
  }

  @Transactional
  public UserResponseDTO create(@Valid CreateUserDTO dto) {
    if (User.existsByEmailActive(dto.email())) {
      throw new EmailAlreadyExistsException(dto.email());
    }

    User user = new User();
    user.name = dto.name();
    user.email = dto.email();
    // user.password = hashPassword(dto.password());
    user.password = dto.password();
    user.role = dto.role();
    user.xp = 0.0;
    user.level = 1;

    user.persist();
    return toResponseDTO(user);
  }

  @Transactional
  public UserResponseDTO update(@NotNull Long id, @Valid UpdateUserDTO dto) {
    User user = User.findActiveById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

    // Verifica email duplicado apenas se estiver sendo alterado
    if (dto.email() != null &&
        !dto.email().equals(user.email) &&
        User.existsByEmailActiveExcludingId(dto.email(), id)) {
      throw new EmailAlreadyExistsException(dto.email());
    }

    // Atualiza apenas campos não nulos
    if (dto.name() != null) {
      user.name = dto.name();
    }
    if (dto.email() != null) {
      user.email = dto.email();
    }
    // if (dto.password() != null && !dto.password().trim().isEmpty()) {
    // user.password = hashPassword(dto.password());
    // }
    if (dto.role() != null) {
      user.role = dto.role();
    }

    user.persist();
    return toResponseDTO(user);
  }

  @Transactional
  public void delete(@NotNull Long id) {
    User user = User.findActiveById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

    user.softDelete();
    user.persist();
  }

  @Transactional
  public UserResponseDTO addExperience(@NotNull Long userId, int points, String reason) {
    User user = User.findActiveById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    user.xp += points;
    updateUserLevel(user);

    user.persist();

    // TODO: Registrar transação de XP
    // xpTransactionService.create(userId, points, reason);

    return toResponseDTO(user);
  }

  public List<UserResponseDTO> findByRole(UserRole role, int page, int size) {
    return User.findByRoleActive(role, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public long count() {
    return User.countActive();
  }

  private UserResponseDTO toResponseDTO(User user) {
    return new UserResponseDTO(
        user.id,
        user.name,
        user.email,
        user.role,
        user.xp,
        user.level,
        user.avatarUrl,
        user.createdAt);
  }

  @SuppressWarnings("unused")
  private void hashPassword(String password) {
    // return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  public void verifyPassword(String password, String hashedPassword) {
    // return BCrypt.checkpw(password, hashedPassword);
  }

  @SuppressWarnings("unused")
  private void updateUserLevel(User user) {
    int newLevel = (int) Math.floor(user.xp / 100.0) + 1;
    if (newLevel > user.level) {
      int oldLevel = user.level;
      user.level = newLevel;

      // TODO: Disparar evento de level up
      // eventPublisher.publishEvent(new UserLevelUpEvent(user.id, oldLevel,
      // newLevel));
    }
  }

}
