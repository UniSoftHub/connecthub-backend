package br.com.hub.connect.application.academic.service;

import java.util.List;
import java.util.stream.Collectors;

import br.com.hub.connect.application.academic.dto.answer.CreateAnswerDTO;
import br.com.hub.connect.application.academic.dto.answer.UpdateAnswerDTO;
import br.com.hub.connect.application.academic.dto.answer.AnswerResponseDTO;
import br.com.hub.connect.domain.academic.enums.TopicStatus;
import br.com.hub.connect.domain.academic.model.Answer;
import br.com.hub.connect.domain.academic.model.Topic;
import br.com.hub.connect.domain.exception.AnswerNotFoundException;
import br.com.hub.connect.domain.exception.TopicNotFoundException;
import br.com.hub.connect.domain.exception.UserNotFoundException;
import br.com.hub.connect.domain.user.model.User;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.ForbiddenException;

@ApplicationScoped
public class AnswerService {

  public List<AnswerResponseDTO> findAll(int page, int size) {
    return Answer.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public AnswerResponseDTO findById(@NotNull Long id) {
    Answer answer = Answer.findActiveById(id)
        .orElseThrow(() -> new AnswerNotFoundException(id));

    return toResponseDTO(answer);
  }

  @Transactional
  public AnswerResponseDTO create(@Valid CreateAnswerDTO dto, SecurityIdentity currentUser) {
    Long authorId = getUserIdFromToken(currentUser);

    Topic topic = Topic.findActiveById(dto.topicId())
        .orElseThrow(() -> new TopicNotFoundException(dto.topicId()));

    User author = User.findActiveById(authorId)
        .orElseThrow(() -> new UserNotFoundException(authorId));

    Answer answer = new Answer();
    answer.content = dto.content();
    answer.topic = topic;
    answer.author = author;
    answer.countLikes = 0;
    answer.countDislikes = 0;
    answer.isSolution = false;

    answer.persist();

    if (topic.status == TopicStatus.NOT_ANSWERED) {
      topic.status = TopicStatus.SOLVED;
      topic.persist();
    }

    return toResponseDTO(answer);
  }

  @Transactional
  public AnswerResponseDTO update(@NotNull Long id, @Valid UpdateAnswerDTO dto, SecurityIdentity currentUser) {
    Answer answer = Answer.findActiveById(id)
        .orElseThrow(() -> new AnswerNotFoundException(id));

    checkUpdatePermissionOrThrow(answer, currentUser);

    if (dto.content() != null) {
      answer.content = dto.content();
    }
    if (dto.isSolution() != null) {
      if (dto.isSolution()) {
        markAsSolution(answer);
      } else {
        answer.isSolution = false;
      }
    }

    answer.persist();
    return toResponseDTO(answer);
  }

  @Transactional
  public void delete(@NotNull Long id, SecurityIdentity currentUser) {
    Answer answer = Answer.findActiveById(id)
        .orElseThrow(() -> new AnswerNotFoundException(id));

    checkDeletePermissionOrThrow(answer, currentUser);

    answer.softDelete();
    answer.persist();

    long remainingAnswers = Answer.countByTopic(answer.topic.id);
    if (remainingAnswers == 0) {
      answer.topic.status = TopicStatus.NOT_ANSWERED;
      answer.topic.persist();
    }
  }

  public List<AnswerResponseDTO> findByTopic(Long topicId, int page, int size) {
    Topic.findActiveById(topicId)
        .orElseThrow(() -> new TopicNotFoundException(topicId));

    return Answer.findByTopicActive(topicId, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public List<AnswerResponseDTO> findByAuthor(Long authorId, int page, int size) {
    User.findActiveById(authorId)
        .orElseThrow(() -> new UserNotFoundException(authorId));

    return Answer.findByAuthorActive(authorId, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public List<AnswerResponseDTO> findWithFilters(Long topicId, Long authorId, int page, int size) {
    if (topicId != null) {
      return findByTopic(topicId, page, size);
    }
    if (authorId != null) {
      return findByAuthor(authorId, page, size);
    }
    return findAll(page, size);
  }

  @Transactional
  public AnswerResponseDTO like(@NotNull Long id) {
    Answer answer = Answer.findActiveById(id)
        .orElseThrow(() -> new AnswerNotFoundException(id));

    answer.incrementLikes();
    answer.persist();

    return toResponseDTO(answer);
  }

  @Transactional
  public AnswerResponseDTO unlike(@NotNull Long id) {
    Answer answer = Answer.findActiveById(id)
        .orElseThrow(() -> new AnswerNotFoundException(id));

    answer.decrementLikes();
    answer.persist();

    return toResponseDTO(answer);
  }

  @Transactional
  public AnswerResponseDTO dislike(@NotNull Long id) {
    Answer answer = Answer.findActiveById(id)
        .orElseThrow(() -> new AnswerNotFoundException(id));

    answer.incrementDislikes();
    answer.persist();

    return toResponseDTO(answer);
  }

  @Transactional
  public AnswerResponseDTO undislike(@NotNull Long id) {
    Answer answer = Answer.findActiveById(id)
        .orElseThrow(() -> new AnswerNotFoundException(id));

    answer.decrementDislikes();
    answer.persist();

    return toResponseDTO(answer);
  }

  @Transactional
  public AnswerResponseDTO markAsSolution(@NotNull Long id) {
    Answer answer = Answer.findActiveById(id)
        .orElseThrow(() -> new AnswerNotFoundException(id));

    markAsSolution(answer);
    answer.persist();

    return toResponseDTO(answer);
  }

  private void markAsSolution(Answer answer) {
    Answer.findSolutionByTopic(answer.topic.id).ifPresent(oldSolution -> {
      oldSolution.isSolution = false;
      oldSolution.persist();
    });

    answer.isSolution = true;

    answer.topic.status = TopicStatus.SOLVED;
    answer.topic.persist();
  }

  public long count() {
    return Answer.countActive();
  }

  public long countByTopic(Long topicId) {
    return Answer.countByTopic(topicId);
  }

  public long countByAuthor(Long authorId) {
    return Answer.countByAuthor(authorId);
  }

  public long countSolutionsByAuthor(Long authorId) {
    return Answer.countSolutionsByAuthor(authorId);
  }

  private Long getUserIdFromToken(SecurityIdentity currentUser) {
    try {
      return Long.parseLong(currentUser.getPrincipal().getName());
    } catch (NumberFormatException e) {
      throw new ForbiddenException("Invalid user principal in token.");
    }
  }

  private void checkUpdatePermissionOrThrow(Answer answer, SecurityIdentity currentUser) {
    Long requestingUserId = getUserIdFromToken(currentUser);
    boolean isOwner = answer.author.id.equals(requestingUserId);
    boolean hasAdminRole = currentUser.hasRole("ADMIN");

    if (!isOwner && !hasAdminRole) {
      throw new ForbiddenException("You do not have permission to update this answer.");
    }
  }

  private void checkDeletePermissionOrThrow(Answer answer, SecurityIdentity currentUser) {
    Long requestingUserId = getUserIdFromToken(currentUser);
    boolean isOwner = answer.author.id.equals(requestingUserId);
    boolean hasAdminRole = currentUser.hasRole("ADMIN");
    boolean hasCoordinatorRole = currentUser.hasRole("COORDINATOR");

    if (!isOwner && !hasAdminRole && !hasCoordinatorRole) {
      throw new ForbiddenException("You do not have permission to delete this answer.");
    }
  }

  private AnswerResponseDTO toResponseDTO(Answer answer) {
    return new AnswerResponseDTO(
        answer.id,
        answer.content,
        answer.topic.id,
        answer.topic.title,
        answer.author.id,
        answer.author.name,
        answer.countLikes,
        answer.countDislikes,
        answer.isSolution,
        answer.createdAt);
  }
}
