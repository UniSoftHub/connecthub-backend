package br.com.hub.connect.domain.communication.model;

import java.util.List;
import java.util.Optional;

import br.com.hub.connect.domain.communication.enums.NotificationType;
import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
@SequenceGenerator(name = "notifications_seq", allocationSize = 1)

public class Notification extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  public User user;

  @Enumerated(EnumType.STRING)
  public NotificationType type;

  @Column(nullable = false)
  public String title;

  @Column(columnDefinition = "TEXT")
  public String message;

  @Column(name = "reference_id")
  public Long referenceId;

  @Column(name = "is_read", nullable = false)
  public Boolean read = false;

  @Override
  public String toString() {
    return String.format("Notification{id=%d, user_id=%d, type='%s', title='%s', read=%b}",
        id, user.id, type, title, read);
  }

  public static List<Notification> findAllActive(int page, int size) {
    return find("isActive = true")
        .page(page, size)
        .list();
  }

  public static Optional<Notification> findActiveById(Long id) {
    return find("id = ?1 and isActive = true", id)
        .firstResultOptional();
  }

  public static long countActive() {
    return count("isActive = true");
  }

}
