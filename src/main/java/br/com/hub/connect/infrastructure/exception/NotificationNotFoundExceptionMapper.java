package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.domain.exception.NotificationNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class NotificationNotFoundExceptionMapper implements ExceptionMapper<NotificationNotFoundException> {
  private static final Logger logger = LoggerFactory.getLogger(NotificationNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(NotificationNotFoundException exception) {
    logger.warn("Notification not found: {}", exception.getMessage());

    ErrorResponse error = new ErrorResponse(
        "NOTIFICATION_NOT_FOUND",
        exception.getMessage(),
        404);

    return Response.status(404).entity(error).build();
  }

}
