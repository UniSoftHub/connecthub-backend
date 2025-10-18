package br.com.hub.connect.domain.gamification.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import io.quarkus.panache.common.Page;

import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_badges")
@SequenceGenerator(name = "user_badges_seq", allocationSize = 1)

public class UserBadge extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  public User user;

  @ManyToOne
  @JoinColumn(name = "badge_id", nullable = false)
  public Badge badge;

  @Column(name = "earned_at", nullable = false)
  public LocalDateTime earnedAt;

  public static List<UserBadge> findAllActive(int page, int size) {
    return find("isActive = true")
        .page(Page.of(page, size))
        .list();
  }

  public static Optional<UserBadge> findActiveById(Long id) {
    return find("id = ?1 and isActive = true", id)
        .firstResultOptional();
  }

  public static long countActive() {
    return count("isActive = true");
  }

  public static List<UserBadge> findByCriteriaActive(String criteria, int page, int size) {
    return find("LOWER(badge.criteria) LIKE LOWER(?1) and isActive = true", "%" + criteria + "%")
        .page(Page.of(page, size))
        .list();
  }

  public static boolean existsByUserAndBadgeActive(Long userId, Long badgeId) {
    return count("user.id = ?1 and badge.id = ?2 and isActive = true", userId, badgeId) > 0;
  }

  public static List<UserBadge> findByNameContaining(String name, int page, int size) {
    return find("LOWER(badge.name) LIKE LOWER(?1) and isActive = true", "%" + name + "%")
        .page(Page.of(page, size))
        .list();
  }

  public static List<UserBadge> findAllOrderByName(int page, int size) {
    return find("isActive = true ORDER BY badge.name ASC")
        .page(Page.of(page, size))
        .list();
  }

  public static List<UserBadge> findByUserId(Long userId) {
    return find("user.id = ?1 and isActive = true", userId).list();
  }

  public static List<UserBadge> findByBadgeId(Long badgeId) {
    return find("badge.id = ?1 and isActive = true", badgeId).list();
  }

  @Override
  public String toString() {
    return String.format("UserBadge{id=%d, user_id=%d, badge_id=%d, earned_at=%s}",
        id, user.id, badge.id, earnedAt.toString());
  }
}
