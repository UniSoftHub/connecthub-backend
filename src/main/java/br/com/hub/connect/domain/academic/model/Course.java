package br.com.hub.connect.domain.academic.model;

import br.com.hub.connect.domain.shared.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
@SequenceGenerator(name = "courses_seq", allocationSize = 1)
public class Course extends BaseEntity {

  @Column(nullable = false)
  public String name;

  @Column(nullable = false, unique = true)
  public String code;

  @Column(nullable = false)
  public Integer semester;

  public Integer workload;

  @Override
  public String toString() {
    return String.format("Course{id=%d, name='%s', code='%s', semester=%s}",
        id, name, code, semester);
  }

}
