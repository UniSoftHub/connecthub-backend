package br.com.hub.connect.application.project.projectComment.service;

import java.util.List;
import java.util.stream.Collectors;
import br.com.hub.connect.application.project.projectComment.dto.ProjectCommentResponseDTO;
import br.com.hub.connect.application.project.projectComment.dto.UpdateProjectCommentDTO;
import br.com.hub.connect.application.project.projectComment.dto.CreateProjectCommentDTO;
import br.com.hub.connect.domain.exception.ProjectCommentNotFoundException;
import br.com.hub.connect.domain.exception.UserNotFoundException;
import br.com.hub.connect.domain.project.model.ProjectComment;
import br.com.hub.connect.domain.project.model.Project;
import br.com.hub.connect.domain.user.model.User;
import br.com.hub.connect.application.project.project.service.ProjectService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class ProjectCommentService {

  private final ProjectService projectService;

  @Inject
  public ProjectCommentService(ProjectService projectService) {
    this.projectService = projectService;
  }

  public List<ProjectCommentResponseDTO> findByProjectId(@NotNull Long projectId, int page, int size) {

    if (page < 0)
      page = 0;
    if (size <= 0 || size > 100)
      size = 10;

    projectService.findById(projectId);

    return ProjectComment.findByProjectId(projectId, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public List<ProjectCommentResponseDTO> findAllGlobal(int page, int size) {

    if (page < 0)
      page = 0;
    if (size <= 0 || size > 100)
      size = 10;

    return ProjectComment.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public ProjectCommentResponseDTO findByIdGlobal(@NotNull Long commentId) {
    ProjectComment comment = ProjectComment.findActiveById(commentId)
        .orElseThrow(() -> new ProjectCommentNotFoundException(commentId));
    return toResponseDTO(comment);
  }

  public ProjectComment findActiveCommentEntityById(@NotNull Long projectId, @NotNull Long commentId) {
    ProjectComment comment = ProjectComment.findActiveById(commentId)
        .orElseThrow(() -> new ProjectCommentNotFoundException(commentId));

    if (!comment.project.id.equals(projectId)) {
      throw new ProjectCommentNotFoundException(commentId);
    }

    return comment;
  }

  public ProjectCommentResponseDTO findCommentByIdAndProjectId(@NotNull Long projectId, @NotNull Long commentId) {

    ProjectComment comment = findActiveCommentEntityById(projectId, commentId);

    return toResponseDTO(comment);
  }

  @Transactional
  public ProjectCommentResponseDTO create(@NotNull Long projectId, @Valid CreateProjectCommentDTO dto) {

    projectService.findById(projectId);

    Project project = Project.findById(projectId);

    User author = (User) User.findActiveById(dto.authorId())
        .orElseThrow(() -> new UserNotFoundException(dto.authorId()));

    ProjectComment comment = new ProjectComment();
    comment.text = dto.text();
    comment.project = project;
    comment.author = author;

    comment.persistAndFlush();
    return toResponseDTO(comment);
  }

  @Transactional
  public ProjectCommentResponseDTO update(@NotNull Long projectId, @NotNull Long commentId,
      @Valid UpdateProjectCommentDTO dto) {

    ProjectComment comment = findActiveCommentEntityById(projectId, commentId);

    if (dto.text() != null) {
      comment.text = dto.text();
    }

    if (dto.authorId() != null) {
      User newAuthor = (User) User.findActiveById(dto.authorId())
          .orElseThrow(() -> new UserNotFoundException(dto.authorId()));
      comment.author = newAuthor;
    }

    comment.persistAndFlush();
    return toResponseDTO(comment);
  }

  @Transactional
  public ProjectCommentResponseDTO updateGlobal(@NotNull Long commentId, @Valid UpdateProjectCommentDTO dto) {

    ProjectComment comment = ProjectComment.findActiveById(commentId)
        .orElseThrow(() -> new ProjectCommentNotFoundException(commentId));

    if (dto.text() != null) {
      comment.text = dto.text();
    }
    if (dto.authorId() != null) {
      User newAuthor = (User) User.findActiveById(dto.authorId())
          .orElseThrow(() -> new UserNotFoundException(dto.authorId()));
      comment.author = newAuthor;
    }

    comment.persistAndFlush();
    return toResponseDTO(comment);
  }

  @Transactional
  public void delete(@NotNull Long projectId, @NotNull Long id) {
    ProjectComment comment = findActiveCommentEntity(projectId, id);
    comment.softDelete();
  }

  @Transactional
  public void deleteGlobal(@NotNull Long commentId) {
    ProjectComment comment = ProjectComment.findActiveById(commentId)
        .orElseThrow(() -> new ProjectCommentNotFoundException(commentId));
    comment.softDelete();
  }

  public long countByProjectId(@NotNull Long projectId) {

    projectService.findById(projectId);

    return ProjectComment.count("project.id", projectId);
  }

  public long countAllGlobal() {
    return ProjectComment.countActive();
  }

  ProjectComment findActiveCommentEntity(@NotNull Long projectId, @NotNull Long commentId) {
    ProjectComment comment = ProjectComment.findActiveById(commentId)
        .orElseThrow(() -> new ProjectCommentNotFoundException(commentId));

    if (!comment.project.id.equals(projectId)) {
      throw new ProjectCommentNotFoundException(commentId);
    }

    return comment;
  }

  private ProjectCommentResponseDTO toResponseDTO(ProjectComment comment) {
    return new ProjectCommentResponseDTO(
        comment.id,
        comment.text,
        comment.author.id,
        comment.createdAt,
        comment.project.id);
  }
}