package br.com.hub.connect.domain.academic.model;

import br.com.hub.connect.domain.user.model.User;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Optional;
import br.com.hub.connect.domain.shared.model.BaseEntity;

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
  public Integer countLikes = 0;

  @Column(name = "count_dislikes", nullable = false)
  public Integer countDislikes = 0;

  @Column(name = "is_solution", nullable = false)
  public Boolean isSolution = false;

  // Métodos Panache Active Record
  public static List<Answer> findAllActive(int page, int size) {
    return find("isActive = true", Sort.by("createdAt").descending())
        .page(Page.of(page, size))
        .list();
  }

  public static Optional<Answer> findActiveById(Long id) {
    return find("id = ?1 and isActive = true", id).firstResultOptional();
  }

  public static long countActive() {
    return count("isActive = true");
  }

  public static List<Answer> findByTopicActive(Long topicId, int page, int size) {
    return find("topic.id = ?1 and isActive = true", Sort.by("createdAt").descending())
        .page(Page.of(page, size))
        .list();
  }

  public static List<Answer> findByAuthorActive(Long authorId, int page, int size) {
    return find("author.id = ?1 and isActive = true", Sort.by("createdAt").descending())
        .page(Page.of(page, size))
        .list();
  }

  public static List<Answer> findSolutionsByTopic(Long topicId) {
    return find("topic.id = ?1 and isSolution = true and isActive = true", topicId).list();
  }

  public static Optional<Answer> findSolutionByTopic(Long topicId) {
    return find("topic.id = ?1 and isSolution = true and isActive = true", topicId).firstResultOptional();
  }

  public static List<Answer> findTopByLikes(int limit) {
    return find("isActive = true", Sort.by("countLikes").descending())
        .page(Page.of(0, limit))
        .list();
  }

  public static long countByTopic(Long topicId) {
    return count("topic.id = ?1 and isActive = true", topicId);
  }

  public static long countByAuthor(Long authorId) {
    return count("author.id = ?1 and isActive = true", authorId);
  }

  public static long countSolutionsByAuthor(Long authorId) {
    return count("author.id = ?1 and isSolution = true and isActive = true", authorId);
  }

  public void incrementLikes() {
    this.countLikes++;
  }

  public void decrementLikes() {
    if (this.countLikes > 0) {
      this.countLikes--;
    }
  }

  public void incrementDislikes() {
    this.countDislikes++;
  }

  public void decrementDislikes() {
    if (this.countDislikes > 0) {
      this.countDislikes--;
    }
  }

  @Override
  public String toString() {
    return String.format("Answer{id=%d, content='%s', topic_id='%s', author_id=%s}",
        id, content, topic.id, author.id);
  }
}
