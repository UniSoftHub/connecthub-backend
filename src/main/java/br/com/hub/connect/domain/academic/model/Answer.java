package br.com.hub.connect.domain.academic.model;

import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "answers")
public class Answer extends BaseEntity {

  @Column(nullable = false, columnDefinition = "TEXT")
  public String content;

  @ManyToOne
  @JoinColumn(name = "topic_id", nullable = false)
  public Topic topic;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  public User author;

  @Column(name = "count_likes", nullable = false)
  public int countLikes = 0;

  @Column(name = "count_dislikes", nullable = false)
  public int countDislikes = 0;

  @Column(name = "is_solution", nullable = false)
  public Boolean isSolution = false;

  @Override
  public String toString() {
    return String.format("Answer{id=%d, content='%s', topic_id='%s', author_id=%s}",
        id, content, topic.id, author.id);
  }

}
