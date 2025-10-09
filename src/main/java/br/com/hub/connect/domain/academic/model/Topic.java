
package br.com.hub.connect.domain.academic.model;

import br.com.hub.connect.domain.academic.enums.TopicStatus;
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
@Table(name = "topics")
@SequenceGenerator(name = "topics_seq", allocationSize = 1)

public class Topic extends BaseEntity {

  @Column(nullable = false)
  public String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  public String content;

  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  public Course course;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  public User author;

  @Enumerated(EnumType.STRING)
  @Column(name = "topic_status", nullable = false)
  public TopicStatus status = TopicStatus.NOT_ANSWERED;

  @Column(name = "count_views", nullable = false)
  public int countViews = 0;

  @Override
  public String toString() {
    return String.format("Topic{id=%d, title='%s', course_id='%s', author_id=%s}",
        id, title, course.id, author.id);
  }

}
