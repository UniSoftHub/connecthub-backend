package br.com.hub.connect.application.gamification.badge.service;

import java.util.List;
import java.util.stream.Collectors;

import br.com.hub.connect.application.gamification.badge.dto.CreateBadgeDTO;
import br.com.hub.connect.application.gamification.badge.dto.ResponseBadgeDTO;
import br.com.hub.connect.application.gamification.badge.dto.UpdateBadgeDTO;
import br.com.hub.connect.domain.exception.BadgeNotFoundException;
import br.com.hub.connect.domain.gamification.model.Badge;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class BadgeService {

  public List<ResponseBadgeDTO> findAll(int page, int size) {
    return Badge.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public ResponseBadgeDTO findById(@NotNull Long id) {
    Badge badge = Badge.findActiveById(id)
        .orElseThrow(() -> new BadgeNotFoundException(id));

    return toResponseDTO(badge);
  }

  @Transactional
  public ResponseBadgeDTO create(@Valid CreateBadgeDTO dto) {

    Badge badge = new Badge();
    badge.name = dto.name();
    badge.description = dto.description();
    badge.imageUrl = dto.imageUrl();
    badge.criteria = dto.criteria();

    badge.persist();
    return toResponseDTO(badge);
  }

  @Transactional
  public ResponseBadgeDTO update(@NotNull Long id, @Valid UpdateBadgeDTO dto) {
    Badge badge = Badge.findActiveById(id)
        .orElseThrow(() -> new BadgeNotFoundException(id));

    if (dto.name() != null) {
      badge.name = dto.name();
    }
    if (dto.description() != null) {
      badge.description = dto.description();
    }
    if (dto.imageUrl() != null) {
      badge.imageUrl = dto.imageUrl();
    }
    if (dto.criteria() != null) {
      badge.criteria = dto.criteria();
    }

    badge.persist();
    return toResponseDTO(badge);
  }

  @Transactional
  public void delete(@NotNull Long id) {
    Badge badge = Badge.findActiveById(id)
        .orElseThrow(() -> new BadgeNotFoundException(id));

    badge.softDelete();
    badge.persist();
  }

  public List<ResponseBadgeDTO> findByCriteria(String criteria, int page, int size) {
    return Badge.findByCriteriaActive(criteria, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public List<ResponseBadgeDTO> findByNameContaining(String name, int page, int size) {
    return Badge.findByNameContaining(name, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public long count() {
    return Badge.countActive();
  }

  private ResponseBadgeDTO toResponseDTO(Badge badge) {
    return new ResponseBadgeDTO(
        badge.id,
        badge.name,
        badge.description,
        badge.imageUrl,
        badge.criteria,
        badge.createdAt,
        badge.updatedAt);
  }
}
