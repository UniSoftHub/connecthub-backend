package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.application.utils.ApiResponse;
import br.com.hub.connect.domain.exception.UserNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException> {
  private static final Logger logger = LoggerFactory.getLogger(UserNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(UserNotFoundException exception) {
    logger.warn("User not found: {}", exception.getMessage());

    ErrorResponse errorDetails = new ErrorResponse(
        "USER_NOT_FOUND",
        exception.getMessage(),
        404);

    ApiResponse<ErrorResponse> response = ApiResponse.error(
        "User Not Found",
        errorDetails);

    return Response.status(404).entity(response).build();
  }
}
