package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.domain.exception.ProjectCommentNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ProjectCommentNotFoundExceptionMapper implements ExceptionMapper<ProjectCommentNotFoundException> {

  private static final Logger logger = LoggerFactory.getLogger(ProjectCommentNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(ProjectCommentNotFoundException exception) {
    logger.warn("Project Comment not found: {}", exception.getMessage());

    ErrorResponse error = new ErrorResponse(
        "PROJECT_COMMENT_NOT_FOUND",
        exception.getMessage(),
        404);

    return Response.status(404).entity(error).build();
  }
}
