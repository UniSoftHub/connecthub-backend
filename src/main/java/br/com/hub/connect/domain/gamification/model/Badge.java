package br.com.hub.connect.domain.gamification.model;

import br.com.hub.connect.domain.shared.model.BaseEntity;
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

  @Override
  public String toString() {
    return String.format("Badge{id=%d, name='%s'}", id, name);
  }

}
