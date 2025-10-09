package br.com.hub.connect.domain.gamification.model;

import java.util.List;
import java.util.Optional;

import br.com.hub.connect.domain.gamification.enums.XpType;
import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.model.User;
import io.quarkus.panache.common.Page;
import jakarta.persistence.EnumType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "xp_transactions")
@SequenceGenerator(name = "xp_transactions_seq", allocationSize = 1)

public class XpTransaction extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  public User user;

  @Column(nullable = false)
  public int amount;

  @Column(nullable = false, columnDefinition = "TEXT")
  public String description;

  @Column(name = "reference_id", nullable = false)
  public Long referenceId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  public XpType type;

  public static List<XpTransaction> findAllActive(int page, int size) {
    return find("isActive = true")
        .page(Page.of(page, size))
        .list();
  }

  public static Optional<XpTransaction> findActiveById(Long id) {
    return find("id = ?1 and isActive = true", id)
        .firstResultOptional();
  }

  public static long countActive() {
    return count("isActive = true");
  }

  public static List<XpTransaction> findByUserIdActive(Long userId, int page, int size) {
    return find("user.id = ?1 and isActive = true", userId)
        .page(Page.of(page, size))
        .list();
  }

  public static boolean existsByReferenceIdAndTypeActive(Long referenceId, XpType type) {
    return count("referenceId = ?1 and type = ?2 and isActive = true", referenceId, type) > 0;
  }

  public static List<XpTransaction> findByDescriptionContaining(String description, int page, int size) {
    return find("LOWER(description) LIKE LOWER(?1) and isActive = true", "%" + description + "%")
        .page(Page.of(page, size))
        .list();
  }

  public static List<XpTransaction> findAllOrderByAmount(int page, int size) {
    return find("isActive = true ORDER BY amount DESC")
        .page(Page.of(page, size))
        .list();
  }

  public static List<XpTransaction> findAllOrderByName(int page, int size) {
    return find("isActive = true ORDER BY user.name ASC")
        .page(Page.of(page, size))
        .list();
  }

  @Override
  public String toString() {
    return String.format("XpTransaction{id=%d, user_id=%d, amount=%d, description='%s'}",
        id, user.id, amount, description);
  }

}
