package br.com.hub.connect.infrastructure.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.hub.connect.domain.exception.CourseNotFoundException;

@Provider
public class CourseNotFoundExceptionMapper implements ExceptionMapper<CourseNotFoundException> {

  private static final Logger logger = LoggerFactory.getLogger(CourseNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(CourseNotFoundException exception) {
    logger.warn("Course not found: {}", exception.getMessage());

    ErrorResponse error = new ErrorResponse(
        "COURSE_NOT_FOUND",
        exception.getMessage(),
        404);

    return Response.status(404).entity(error).build();
  }
}
