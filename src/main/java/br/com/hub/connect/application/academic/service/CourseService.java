package br.com.hub.connect.application.academic.service;

import java.util.List;
import java.util.stream.Collectors;

import br.com.hub.connect.application.academic.dto.course.CreateCourseDTO;
import br.com.hub.connect.application.academic.dto.course.UpdateCourseDTO;
import br.com.hub.connect.application.academic.dto.course.CourseResponseDTO;
import br.com.hub.connect.domain.academic.model.Course;
import br.com.hub.connect.domain.exception.CourseNotFoundException;
import br.com.hub.connect.domain.exception.CodeAlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class CourseService {

  public List<CourseResponseDTO> findAll(int page, int size) {
    return Course.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public CourseResponseDTO findById(@NotNull Long id) {
    Course course = Course.findActiveById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));

    return toResponseDTO(course);
  }

  @Transactional
  public CourseResponseDTO create(@Valid CreateCourseDTO dto) {
    if (Course.existsByCode(dto.code())) {
      throw new CodeAlreadyExistsException(dto.code());
    }

    Course course = new Course();
    course.name = dto.name();
    course.code = dto.code();
    course.semester = dto.semester();
    course.workload = dto.workload();

    course.persist();
    return toResponseDTO(course);
  }

  @Transactional
  public CourseResponseDTO update(@NotNull Long id, @Valid UpdateCourseDTO dto) {
    Course course = Course.findActiveById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));

    // Verifica código duplicado apenas se estiver sendo alterado
    if (dto.code() != null &&
        !dto.code().equals(course.code) &&
        Course.existsByCodeActiveExcludingId(dto.code(), id)) {
      throw new CodeAlreadyExistsException(dto.code());
    }

    // Atualiza apenas campos não nulos
    if (dto.name() != null) {
      course.name = dto.name();
    }
    if (dto.code() != null) {
      course.code = dto.code();
    }
    if (dto.semester() != null) {
      course.semester = dto.semester();
    }
    if (dto.workload() != null) {
      course.workload = dto.workload();
    }

    course.persist();
    return toResponseDTO(course);
  }

  @Transactional
  public void delete(@NotNull Long id) {
    Course course = Course.findActiveById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));

    course.softDelete();
    course.persist();
  }

  public List<CourseResponseDTO> findBySemester(Integer semester, int page, int size) {
    return Course.findBySemesterActive(semester, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public long count() {
    return Course.countActive();
  }

  private CourseResponseDTO toResponseDTO(Course course) {
    return new CourseResponseDTO(
        course.id,
        course.name,
        course.code,
        course.semester,
        course.workload,
        course.createdAt);
  }
}
