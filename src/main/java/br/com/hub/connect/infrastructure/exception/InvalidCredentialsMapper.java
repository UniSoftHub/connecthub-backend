package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.application.utils.ApiResponse;
import br.com.hub.connect.domain.exception.InvalidCredentialsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidCredentialsMapper implements ExceptionMapper<InvalidCredentialsException> {

  private static final Logger logger = LoggerFactory.getLogger(InvalidCredentialsMapper.class);

  @Override
  public Response toResponse(InvalidCredentialsException exception) {
    logger.warn("Invalid credentials: {}", exception.getMessage());

    ErrorResponse errorDetails = new ErrorResponse(
        "INVALID_CREDENTIALS",
        exception.getMessage(),
        401);

    ApiResponse<ErrorResponse> response = ApiResponse.error(
        "Invalid Credentials",
        errorDetails);

    return Response.status(401).entity(response).build();
  }
}
