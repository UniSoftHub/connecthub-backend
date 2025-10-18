package br.com.hub.connect.domain.gamification.model;

import java.util.List;
import java.util.Optional;

import br.com.hub.connect.domain.shared.model.BaseEntity;
import io.quarkus.panache.common.Page;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "badges")
@SequenceGenerator(name = "badges_seq", allocationSize = 1)

public class Badge extends BaseEntity {

  @Column(nullable = false)
  public String name;

  @Column(nullable = false, columnDefinition = "TEXT")
  public String description;

  @Column(name = "image_url", nullable = false)
  public String imageUrl;

  @Column(nullable = false, columnDefinition = "TEXT")
  public String criteria;

  public static List<Badge> findAllActive(int page, int size) {
    return find("isActive = true")
        .page(Page.of(page, size))
        .list();
  }

  public static Optional<Badge> findActiveById(Long id) {
    return find("id = ?1 and isActive = true", id)
        .firstResultOptional();
  }

  public static long countActive() {
    return count("isActive = true");
  }

  public static List<Badge> findByCriteriaActive(String criteria, int page, int size) {
    return find("LOWER(criteria) LIKE LOWER(?1) and isActive = true", "%" + criteria + "%")
        .page(Page.of(page, size))
        .list();
  }

  public static boolean existsByNameActive(String name) {
    return count("name = ?1 and isActive = true", name) > 0;
  }

  public static List<Badge> findByNameContaining(String name, int page, int size) {
    return find("LOWER(name) LIKE LOWER(?1) and isActive = true", "%" + name + "%")
        .page(Page.of(page, size))
        .list();
  }

  public static List<Badge> findAllOrderByName(int page, int size) {
    return find("isActive = true ORDER BY name ASC")
        .page(Page.of(page, size))
        .list();
  }

  @Override
  public String toString() {
    return String.format("Badge{id=%d, name='%s'}", id, name);
  }
}
