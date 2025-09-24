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
        return Project.countActive();
    }
    private ProjectCommentResponseDTO toResponseDTO(ProjectComment comment) {
        return new ProjectCommentResponseDTO(
            comment.id,
            comment.text,
            comment.author.id,
            comment.createdAt,
            comment.project.id
        );
    }  

    public List<ProjectCommentResponseDTO> listComments(String projectId) {
        // TODO: Implement the logic to fetch comments for the given projectId
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
