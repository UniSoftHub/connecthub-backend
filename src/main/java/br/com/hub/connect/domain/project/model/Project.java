package br.com.hub.connect.domain.project.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.com.hub.connect.domain.project.enums.ProjectTechnologies;
import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.model.User;
import io.quarkus.panache.common.Page;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "projects")
@SequenceGenerator(name = "projects_seq", allocationSize = 1)

@SequenceGenerator(name = "projects_seq", allocationSize = 1)
public class Project extends BaseEntity {

  @Column(nullable = false)
  public String name;

  @Column(nullable = false)
  public String description;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  public User author;

  @Column(name = "repository_url", nullable = false)
  public String repositoryUrl;

  @Column(name = "image_url")
  public String imageUrl;

  @Enumerated(EnumType.STRING)
  @ElementCollection(targetClass = ProjectTechnologies.class)
  @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
  @Column(name = "technology")
  public Set<ProjectTechnologies> technologies;

  @Column(name = "count_views", nullable = false)
  public int countViews = 0;

  @Override
  public String toString() {
    return String.format("Project{id=%d, name='%s', description='%s', repository_url=%s}",
        id, name, description, repositoryUrl);
  }

  public static List<Project> findAllActive(int page, int size) {
    return find("isActive = true")
        .page(Page.of(page, size))
        .list();
  }

  public static Optional<Project> findActiveById(Long id) {
    return find("id = ?1 and isActive = true", id)
        .firstResultOptional();
  }

  public static long countActive() {
    return count("isActive = true");
  }
}
