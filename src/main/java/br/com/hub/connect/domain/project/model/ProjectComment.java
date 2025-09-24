package br.com.hub.connect.domain.project.model;

import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "project_comments")
public class ProjectComment extends BaseEntity {

  @Column(nullable = false, columnDefinition = "TEXT")
  public String text;

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  public Project project;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  public User author;

  @Override
  public String toString() {
    return String.format("ProjectComment{id=%d, text='%s', project_id='%s', author_id=%s}",
        id, text, project.id, author.id);
  }

}
