package br.com.hub.connect.domain.gamification.model;

import java.time.LocalDateTime;

import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_badges")
public class UserBadge extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  public User user;

  @ManyToOne
  @JoinColumn(name = "badge_id", nullable = false)
  public Badge badge;

  @Column(name = "earned_at", nullable = false)
  public LocalDateTime earnedAt;

  @Override
  public String toString() {
    return String.format("UserBadge{id=%d, user_id=%d, badge_id=%d, earned_at=%s}",
        id, user.id, badge.id, earnedAt.toString());
  }
}
