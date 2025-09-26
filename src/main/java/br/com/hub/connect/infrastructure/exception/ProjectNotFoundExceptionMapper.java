package br.com.hub.connect.infrastructure.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.domain.exception.ProjectNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class ProjectNotFoundExceptionMapper implements ExceptionMapper<ProjectNotFoundException> {

    private static final Logger logger = LoggerFactory.getLogger(UserNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(ProjectNotFoundException exception) {
    logger.warn("Project not found: {}", exception.getMessage());

    ErrorResponse error = new ErrorResponse(
        "PROJECT_NOT_FOUND",
        exception.getMessage(),
        404);

    return Response.status(404).entity(error).build();
  }
}
