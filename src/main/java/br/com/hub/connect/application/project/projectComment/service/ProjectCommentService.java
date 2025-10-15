package br.com.hub.connect.application.project.projectComment.service;

import java.util.List;
import java.util.stream.Collectors;

import br.com.hub.connect.application.project.projectComment.dto.ProjectCommentResponseDTO;
import br.com.hub.connect.application.project.projectComment.dto.UpdateProjectCommentDTO;
import br.com.hub.connect.domain.exception.ProjectCommentNotFoundException;
import br.com.hub.connect.domain.project.model.ProjectComment;
import br.com.hub.connect.application.project.projectComment.dto.CreateProjectCommentDTO;
import br.com.hub.connect.domain.user.model.User;
import br.com.hub.connect.domain.project.model.Project;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@ApplicationScoped
public class ProjectCommentService {
  public List<ProjectCommentResponseDTO> findAll(int page, int size) {
    return ProjectComment.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public ProjectCommentResponseDTO findById(@NotNull Long id) {
    ProjectComment comment = ProjectComment.findActiveById(id)
        .orElseThrow(() -> new ProjectCommentNotFoundException(id));

    return toResponseDTO(comment);
  }

  @Transactional
  public ProjectCommentResponseDTO create(@Valid CreateProjectCommentDTO dto) {

    ProjectComment comment = new ProjectComment();
    comment.text = dto.text();
    comment.project = Project.findById(dto.projectId());
    comment.author = User.findById(dto.authorId());

    comment.persist();
    return toResponseDTO(comment);
  }

  @Transactional
  public void update(@NotNull @Positive Long id, @Valid UpdateProjectCommentDTO dto) {
    ProjectComment comment = ProjectComment.findActiveById(id)
        .orElseThrow(() -> new ProjectCommentNotFoundException(id));

    if (dto.text() != null) {
      comment.text = dto.text();
    }
    if (dto.projectId() != null) {
      comment.project = Project.findById(dto.projectId());
    }
    if (dto.authorId() != null) {
      comment.author = User.findById(dto.authorId());
    }

    comment.persist();
    // return toResponseDTO(comment);
  }

  @Transactional
  public void delete(@NotNull @Positive Long id) {
    ProjectComment comment = ProjectComment.findActiveById(id)
        .orElseThrow(() -> new ProjectCommentNotFoundException(id));
    comment.softDelete();
  }

  public long count() {
    return ProjectComment.countActive();
  }

  private ProjectCommentResponseDTO toResponseDTO(ProjectComment comment) {
    return new ProjectCommentResponseDTO(
        comment.id,
        comment.text,
        comment.author.id,
        comment.createdAt,
        comment.project.id);
  }

  public List<ProjectCommentResponseDTO> listComments(String projectId) {
    try {
      Long id = Long.parseLong(projectId);
      return findByProjectId(id, 0, Integer.MAX_VALUE); // Retorna todos os comentários
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid project ID format: " + projectId);
    }
  }

  public List<ProjectCommentResponseDTO> findByProjectId(@NotNull Long projectId, int page, int size) {
    // Validações básicas
    if (projectId == null || projectId <= 0) {
      throw new IllegalArgumentException("Project ID must be a positive number");
    }

    // Normalizar paginação
    if (page < 0)
      page = 0;
    if (size <= 0 || size > 100)
      size = 10;

    // Verificar se o projeto existe (opcional)
    Project project = Project.findById(projectId);
    if (project == null) {
      throw new IllegalArgumentException("Project with ID " + projectId + " not found");
    }

    return ProjectComment.findByProjectId(projectId, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }
}
