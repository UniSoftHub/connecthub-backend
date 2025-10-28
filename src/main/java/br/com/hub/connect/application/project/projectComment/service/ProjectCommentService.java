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
import jakarta.ws.rs.NotFoundException;

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

  public ProjectCommentResponseDTO findCommentByNumberAndProjectId(@NotNull Long projectId,
      @NotNull Long commentNumber) {
    ProjectComment comment = findActiveCommentEntityByNumber(projectId, commentNumber);
    return toResponseDTO(comment);
  }

  public ProjectComment findActiveCommentEntityByNumber(@NotNull Long projectId, @NotNull Long projectCommentNumber) {
    return ProjectComment.find(
        "project.id = ?1 AND projectCommentNumber = ?2 AND isActive = true",
        projectId, projectCommentNumber)

        .firstResultOptional()
        .map(entity -> (ProjectComment) entity)
        .orElseThrow(() -> new NotFoundException(
            String.format("Comment with number %d not found for project %d", projectCommentNumber, projectId)));
  }

  @Transactional
  public ProjectCommentResponseDTO create(@NotNull Long projectId, @Valid CreateProjectCommentDTO dto) {

    projectService.findById(projectId);

    Project project = Project.findById(projectId);

    User author = (User) User.findActiveById(dto.authorId())
        .orElseThrow(() -> new UserNotFoundException(dto.authorId()));

    long currentCount = ProjectComment.countByProjectId(projectId);
    long nextCommentNumber = currentCount + 1;

    ProjectComment comment = new ProjectComment();
    comment.text = dto.text();
    comment.project = project;
    comment.author = author;
    comment.projectCommentNumber = nextCommentNumber;

    comment.persistAndFlush();
    return toResponseDTO(comment);
  }

  @Transactional
  public ProjectCommentResponseDTO update(@NotNull Long projectId, @NotNull Long commentNumber,
      @Valid UpdateProjectCommentDTO dto) {

    ProjectComment comment = findActiveCommentEntityByNumber(projectId, commentNumber);

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
  public void delete(@NotNull Long projectId, @NotNull Long commentNumber) {
    ProjectComment comment = findActiveCommentEntityByNumber(projectId, commentNumber);
    comment.softDelete();
  }

  public long countByProjectId(@NotNull Long projectId) {

    projectService.findById(projectId);

    return ProjectComment.count("project.id", projectId);
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
        comment.project.id,
        comment.projectCommentNumber);
  }
}