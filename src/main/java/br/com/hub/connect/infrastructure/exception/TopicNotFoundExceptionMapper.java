
package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.domain.exception.TopicNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TopicNotFoundExceptionMapper implements ExceptionMapper<TopicNotFoundException> {

  private static final Logger logger = LoggerFactory.getLogger(TopicNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(TopicNotFoundException exception) {
    logger.warn("Topic not found: {}", exception.getMessage());

    ErrorResponse error = new ErrorResponse(
        "TOPIC_NOT_FOUND",
        exception.getMessage(),
        404);

    return Response.status(404).entity(error).build();
  }
}
