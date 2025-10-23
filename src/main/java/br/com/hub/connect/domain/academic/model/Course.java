package br.com.hub.connect.domain.academic.model;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Optional;
import br.com.hub.connect.domain.shared.model.BaseEntity;

@Entity
@Table(name = "courses")
public class Course extends BaseEntity {

  @Column(nullable = false)
  public String name;

  @Column(nullable = false, unique = true)
  public String code;

  @Column(nullable = false)
  public Integer semester;

  public Integer workload;

  // Métodos Panache Active Record
  public static List<Course> findAllActive(int page, int size) {
    return find("isActive = true", Sort.by("createdAt").descending())
        .page(Page.of(page, size))
        .list();
  }

  public static Optional<Course> findActiveById(Long id) {
    return find("id = ?1 and isActive = true", id).firstResultOptional();
  }

  public static long countActive() {
    return count("isActive = true");
  }

  public static List<Course> findByCode(String code) {
    return find("code = ?1 and isActive = true", code).list();
  }

  public static List<Course> findBySemester(Integer semester) {
    return find("semester = ?1 and isActive = true", semester).list();
  }

  public static List<Course> findBySemesterActive(Integer semester, int page, int size) {
    return find("semester = ?1 and isActive = true", Sort.by("createdAt").descending())
        .page(Page.of(page, size))
        .list();
  }

  public static List<Course> findByName(String name) {
    return find("lower(name) like lower(?1) and isActive = true", "%" + name + "%").list();
  }

  public static boolean existsByCode(String code) {
    return count("code = ?1 and isActive = true", code) > 0;
  }

  public static boolean existsByCodeActiveExcludingId(String code, Long id) {
    return count("code = ?1 and isActive = true and id != ?2", code, id) > 0;
  }

  @Override
  public String toString() {
    return String.format("Course{id=%d, name='%s', code='%s', semester=%s}",
        id, name, code, semester);
  }
}
