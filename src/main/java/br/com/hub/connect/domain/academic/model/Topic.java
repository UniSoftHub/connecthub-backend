package br.com.hub.connect.domain.academic.model;

import br.com.hub.connect.domain.academic.enums.TopicStatus;
import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.model.User;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
  public Integer countViews = 0;

  // Métodos Panache Active Record

  public static List<Topic> findAllActive(int page, int size) {
    return find("isActive = true", Sort.by("createdAt").descending())
        .page(Page.of(page, size))
        .list();
  }

  public static Optional<Topic> findActiveById(Long id) {
    return find("id = ?1 and isActive = true", id).firstResultOptional();
  }

  public static long countActive() {
    return count("isActive = true");
  }

  public static List<Topic> findByCourseActive(Long courseId, int page, int size) {
    return find("course.id = ?1 and isActive = true", Sort.by("createdAt").descending(), courseId)
        .page(Page.of(page, size))
        .list();
  }

  public static List<Topic> findByAuthorActive(Long authorId, int page, int size) {
    return find("author.id = ?1 and isActive = true", Sort.by("createdAt").descending(), authorId)
        .page(Page.of(page, size))
        .list();
  }

  public static List<Topic> findByStatusActive(TopicStatus status, int page, int size) {
    return find("status = ?1 and isActive = true", Sort.by("createdAt").descending())
        .page(Page.of(page, size))
        .list();
  }

  // --- MÉTODO NOVO ADICIONADO ---
  public static List<Topic> findWithFilters(Long courseId, Long authorId, TopicStatus status, int page, int size) {
    Map<String, Object> params = new HashMap<>();

    // Inicia a query garantindo que só traga registros ativos
    StringBuilder query = new StringBuilder("isActive = true");

    if (courseId != null) {
      query.append(" AND course.id = :courseId");
      params.put("courseId", courseId);
    }

    if (authorId != null) {
      query.append(" AND author.id = :authorId");
      params.put("authorId", authorId);
    }

    if (status != null) {
      query.append(" AND status = :status");
      params.put("status", status);
    }

    return find(query.toString(), Sort.by("createdAt").descending(), params)
        .page(Page.of(page, size))
        .list();
  }
  // ------------------------------

  public static List<Topic> findByTitleContaining(String title) {
    return find("lower(title) like lower(?1) and isActive = true", "%" + title + "%").list();
  }

  public static long countByCourse(Long courseId) {
    return count("course.id = ?1 and isActive = true", courseId);
  }

  public static long countByAuthor(Long authorId) {
    return count("author.id = ?1 and isActive = true", authorId);
  }

  public void incrementViews() {
    this.countViews++;
  }

  @Override
  public String toString() {
    return String.format("Topic{id=%d, title='%s', course_id='%s', author_id=%s}",
        id, title, course.id, author.id);
  }
}
