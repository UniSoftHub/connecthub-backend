package br.com.hub.connect.application.academic.service;

import java.util.List;
import java.util.stream.Collectors;

import br.com.hub.connect.application.academic.dto.topic.CreateTopicDTO;
import br.com.hub.connect.application.academic.dto.topic.TopicResponseDTO;
import br.com.hub.connect.application.academic.dto.topic.UpdateTopicDTO;
import br.com.hub.connect.domain.academic.enums.TopicStatus;
import br.com.hub.connect.domain.academic.model.Course;
import br.com.hub.connect.domain.academic.model.Topic;
import br.com.hub.connect.domain.exception.CourseNotFoundException;
import br.com.hub.connect.domain.exception.TopicNotFoundException;
import br.com.hub.connect.domain.exception.UserNotFoundException;
import br.com.hub.connect.domain.user.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class TopicService {

  public List<TopicResponseDTO> findAll(int page, int size) {
    return Topic.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public TopicResponseDTO findById(@NotNull Long id) {
    Topic topic = Topic.findActiveById(id)
        .orElseThrow(() -> new TopicNotFoundException(id));

    // Incrementa visualizações
    topic.incrementViews();
    topic.persist();

    return toResponseDTO(topic);
  }

  @Transactional
  public TopicResponseDTO create(@Valid CreateTopicDTO dto) {
    // Verifica se o curso existe
    Course course = Course.findActiveById(dto.courseId())
        .orElseThrow(() -> new CourseNotFoundException(dto.courseId()));

    // Verifica se o autor existe
    User author = User.findActiveById(dto.authorId())
        .orElseThrow(() -> new UserNotFoundException(dto.authorId()));

    Topic topic = new Topic();
    topic.title = dto.title();
    topic.content = dto.content();
    topic.course = course;
    topic.author = author;
    topic.status = TopicStatus.NOT_ANSWERED;
    topic.countViews = 0;

    topic.persist();
    return toResponseDTO(topic);
  }

  @Transactional
  public TopicResponseDTO update(@NotNull Long id, @Valid UpdateTopicDTO dto) {
    Topic topic = Topic.findActiveById(id)
        .orElseThrow(() -> new TopicNotFoundException(id));

    // Atualiza apenas campos não nulos
    if (dto.title() != null) {
      topic.title = dto.title();
    }
    if (dto.content() != null) {
      topic.content = dto.content();
    }
    if (dto.status() != null) {
      topic.status = dto.status();
    }

    topic.persist();
    return toResponseDTO(topic);
  }

  @Transactional
  public void delete(@NotNull Long id) {
    Topic topic = Topic.findActiveById(id)
        .orElseThrow(() -> new TopicNotFoundException(id));

    topic.softDelete();
    topic.persist();
  }

  public List<TopicResponseDTO> findByCourse(Long courseId, int page, int size) {
    // Verifica se o curso existe
    Course.findActiveById(courseId)
        .orElseThrow(() -> new CourseNotFoundException(courseId));

    return Topic.findByCourseActive(courseId, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public List<TopicResponseDTO> findByAuthor(Long authorId, int page, int size) {
    // Verifica se o autor existe
    User.findActiveById(authorId)
        .orElseThrow(() -> new UserNotFoundException(authorId));

    return Topic.findByAuthorActive(authorId, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public List<TopicResponseDTO> findByStatus(TopicStatus status, int page, int size) {
    return Topic.findByStatusActive(status, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public long count() {
    return Topic.countActive();
  }

  public long countByCourse(Long courseId) {
    return Topic.countByCourse(courseId);
  }

  public long countByAuthor(Long authorId) {
    return Topic.countByAuthor(authorId);
  }

  private TopicResponseDTO toResponseDTO(Topic topic) {
    return new TopicResponseDTO(
        topic.id,
        topic.title,
        topic.content,
        topic.course.id,
        topic.course.name,
        topic.author.id,
        topic.author.name,
        topic.status,
        topic.countViews,
        topic.createdAt);
  }
}
