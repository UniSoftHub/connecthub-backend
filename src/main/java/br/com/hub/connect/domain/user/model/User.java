package br.com.hub.connect.domain.user.model;

import java.util.Set;

import br.com.hub.connect.domain.gamification.model.UserBadge;
import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.enums.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

  @Enumerated(EnumType.STRING)
  public UserRole role;

  @Column(nullable = false)
  public String name;

  @Column(unique = true, nullable = false)
  public String email;

  @Column(nullable = false)
  public String password;

  public Double xp = 0.0;
  public Integer level = 1;

  @Column(name = "avatar_url")
  public String avatarUrl;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  public Set<UserBadge> badges;

  @Override
  public String toString() {
    return String.format("User{id=%d, name='%s', email='%s', role=%s}",
        id, name, email, role);
  }
}
