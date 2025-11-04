
package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.application.utils.ApiResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
  private static final Logger logger = LoggerFactory.getLogger(GenericExceptionMapper.class);

  @Override
  public Response toResponse(Exception exception) {
    logger.error("Internal Error: {}", exception.getMessage());

    ErrorResponse errorDetails = new ErrorResponse(
        "INTERNAL_ERROR",
        "An unexpected error occurred.",
        500);

    ApiResponse<ErrorResponse> response = ApiResponse.error(
        "Internal Error",
        errorDetails);

    return Response.status(500).entity(response).build();
  }
}
