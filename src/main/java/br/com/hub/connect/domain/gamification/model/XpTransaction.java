package br.com.hub.connect.domain.gamification.model;

import br.com.hub.connect.domain.gamification.enums.XpType;
import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "xp_transactions")
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

  @Override
  public String toString() {
    return String.format("XpTransaction{id=%d, user_id=%d, amount=%d, description='%s'}",
        id, user.id, amount, description);
  }

}
