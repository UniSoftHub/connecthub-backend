package br.com.hub.connect.domain.project.model;

import java.util.List;
import java.util.Optional;

import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.model.User;
import io.quarkus.panache.common.Page;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "project_comments")
@SequenceGenerator(name = "project_comments_seq", allocationSize = 1)

public class ProjectComment extends BaseEntity {

  @Column(nullable = false, columnDefinition = "TEXT")
  public String text;

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  public Project project;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  public User author;

  @Column(nullable = false)
  public Long projectCommentNumber;

  @Override
  public String toString() {
    return String.format("ProjectComment{id=%d, text='%s', project_id='%s', author_id=%s}",
        id, text, project.id, author.id);
  }

  public static List<ProjectComment> findAllActive(int page, int size) {
    return find("isActive = true")
        .page(Page.of(page, size))
        .list();
  }

  public static Optional<ProjectComment> findActiveById(Long id) {
    return find("id = ?1 and isActive = true", id)
        .firstResultOptional();
  }

  public static long countActive() {
    return count("isActive = true");
  }

  public static List<ProjectComment> findByProjectId(Long projectId, int page, int size) {
    return find("project.id = ?1 AND isActive = true", projectId)
        .page(page, size)
        .list();
  }

  public static long countByProjectId(Long projectId) {
    return count("project.id = ?1", projectId);
  }

}
