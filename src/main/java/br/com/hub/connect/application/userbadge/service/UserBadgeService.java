package br.com.hub.connect.application.userbadge.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.hub.connect.application.userbadge.dto.CreateUserBadgeDTO;
import br.com.hub.connect.application.userbadge.dto.ResponseUserBadgeDTO;
import br.com.hub.connect.domain.exception.BadgeNotFoundException;
import br.com.hub.connect.domain.exception.UserBadgeNotFoundException;
import br.com.hub.connect.domain.exception.UserNotFoundException;
import br.com.hub.connect.domain.exception.UserAlreadyHasBadgeException;
import br.com.hub.connect.domain.gamification.model.Badge;
import br.com.hub.connect.domain.gamification.model.UserBadge;
import br.com.hub.connect.domain.user.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class UserBadgeService {

  public List<ResponseUserBadgeDTO> findAll(int page, int size) {
    return UserBadge.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public ResponseUserBadgeDTO findById(@NotNull Long id) {
    UserBadge userBadge = UserBadge.findActiveById(id)
        .orElseThrow(() -> new UserBadgeNotFoundException(id));

    return toResponseDTO(userBadge);
  }

  @Transactional
  public ResponseUserBadgeDTO create(@Valid CreateUserBadgeDTO dto) {
    User user = User.findActiveById(dto.userId())
        .orElseThrow(() -> new UserNotFoundException(dto.userId()));

    Badge badge = Badge.findActiveById(dto.badgeId())
        .orElseThrow(() -> new BadgeNotFoundException(dto.badgeId()));

    if (UserBadge.existsByUserAndBadgeActive(dto.userId(), dto.badgeId())) {
      throw new IllegalArgumentException("User already has this badge");
    }
    if (UserBadge.existsByUserAndBadgeActive(dto.userId(), dto.badgeId())) {
      throw new UserAlreadyHasBadgeException(dto.userId(), dto.badgeId());
    }

    UserBadge userBadge = new UserBadge();
    userBadge.user = user;
    userBadge.badge = badge;
    userBadge.earnedAt = LocalDateTime.now();

    userBadge.persist();
    return toResponseDTO(userBadge);
  }

  @Transactional
  public void delete(@NotNull Long id) {
    UserBadge userBadge = UserBadge.findActiveById(id)
        .orElseThrow(() -> new UserBadgeNotFoundException(id));

    userBadge.softDelete();
    userBadge.persist();
  }

  public List<ResponseUserBadgeDTO> findByUserId(@NotNull Long userId) {
    return UserBadge.findByUserId(userId)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public List<ResponseUserBadgeDTO> findByBadgeId(@NotNull Long badgeId) {
    return UserBadge.findByBadgeId(badgeId)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public long count() {
    return UserBadge.countActive();
  }

  private ResponseUserBadgeDTO toResponseDTO(UserBadge userBadge) {
    return new ResponseUserBadgeDTO(
        userBadge.id,
        userBadge.user.id,
        userBadge.user.name,
        userBadge.badge.id,
        userBadge.badge.name,
        userBadge.badge.description,
        userBadge.badge.imageUrl,
        userBadge.earnedAt,
        userBadge.createdAt,
        userBadge.updatedAt);
  }
}