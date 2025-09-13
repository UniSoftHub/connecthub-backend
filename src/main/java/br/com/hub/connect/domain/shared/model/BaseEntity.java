package br.com.hub.connect.domain.shared.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity extends PanacheEntity {

  @Column(name = "is_active", nullable = false)
  public Boolean isActive = true;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  public LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  public LocalDateTime updatedAt;

  @Column(name = "excluded_at")
  public LocalDateTime excludedAt;

  // Métodos genéricos
  public boolean isActive() {
    return Boolean.TRUE.equals(this.isActive);
  }

  public void softDelete() {
    if (Boolean.FALSE.equals(this.isActive)) {
      return;
    }
    this.isActive = false;
    this.excludedAt = LocalDateTime.now();
  }

  // Métodos estáticos genéricos usando reflection
  public static <T extends BaseEntity> List<T> findActive(Class<T> entityClass) {
    return find("isActive = true").list();
  }

  @SuppressWarnings("unchecked")
  public static <T extends BaseEntity> T findActiveById(Class<T> entityClass, Long id) {
    return (T) find("id = ?1 and isActive = true", id).firstResult();
  }
}
