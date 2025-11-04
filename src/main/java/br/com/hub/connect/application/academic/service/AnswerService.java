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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
  public AnswerResponseDTO create(@Valid CreateAnswerDTO dto) {
    // Verifica se o tópico existe
    Topic topic = Topic.findActiveById(dto.topicId())
        .orElseThrow(() -> new TopicNotFoundException(dto.topicId()));

    // Verifica se o autor existe
    User author = User.findActiveById(dto.authorId())
        .orElseThrow(() -> new UserNotFoundException(dto.authorId()));

    Answer answer = new Answer();
    answer.content = dto.content();
    answer.topic = topic;
    answer.author = author;
    answer.countLikes = 0;
    answer.countDislikes = 0;
    answer.isSolution = false;

    answer.persist();

    // Atualiza status do tópico para ANSWERED se for a primeira resposta
    if (topic.status == TopicStatus.NOT_ANSWERED) {
      topic.status = TopicStatus.SOLVED;
      topic.persist();
    }

    return toResponseDTO(answer);
  }

  @Transactional
  public AnswerResponseDTO update(@NotNull Long id, @Valid UpdateAnswerDTO dto) {
    Answer answer = Answer.findActiveById(id)
        .orElseThrow(() -> new AnswerNotFoundException(id));

    // Atualiza apenas campos não nulos
    if (dto.content() != null) {
      answer.content = dto.content();
    }
    if (dto.isSolution() != null) {
      // Se marcar como solução, desmarca outras soluções do mesmo tópico
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
  public void delete(@NotNull Long id) {
    Answer answer = Answer.findActiveById(id)
        .orElseThrow(() -> new AnswerNotFoundException(id));

    answer.softDelete();
    answer.persist();

    // Verifica se ainda existem respostas ativas para o tópico
    long remainingAnswers = Answer.countByTopic(answer.topic.id);
    if (remainingAnswers == 0) {
      answer.topic.status = TopicStatus.NOT_ANSWERED;
      answer.topic.persist();
    }
  }

  public List<AnswerResponseDTO> findByTopic(Long topicId, int page, int size) {
    // Verifica se o tópico existe
    Topic.findActiveById(topicId)
        .orElseThrow(() -> new TopicNotFoundException(topicId));

    return Answer.findByTopicActive(topicId, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public List<AnswerResponseDTO> findByAuthor(Long authorId, int page, int size) {
    // Verifica se o autor existe
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
    // Remove solução anterior do tópico
    Answer.findSolutionByTopic(answer.topic.id).ifPresent(oldSolution -> {
      oldSolution.isSolution = false;
      oldSolution.persist();
    });

    // Marca como solução
    answer.isSolution = true;

    // Atualiza status do tópico
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
