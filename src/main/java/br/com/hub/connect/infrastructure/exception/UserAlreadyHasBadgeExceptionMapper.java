package br.com.hub.connect.infrastructure.exception;

import br.com.hub.connect.domain.exception.UserAlreadyHasBadgeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserAlreadyHasBadgeExceptionMapper implements ExceptionMapper<UserAlreadyHasBadgeException> {
  private static final Logger logger = LoggerFactory.getLogger(UserAlreadyHasBadgeExceptionMapper.class);

  @Override
  public Response toResponse(UserAlreadyHasBadgeException exception) {
    logger.warn("User already has badge: {}", exception.getMessage());

    ErrorResponse error = new ErrorResponse(
        "USER_ALREADY_HAS_BADGE",
        exception.getMessage(),
        409);

    return Response.status(409).entity(error).build();
  }
}
