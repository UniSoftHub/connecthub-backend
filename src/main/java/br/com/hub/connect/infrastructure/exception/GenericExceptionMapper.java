
package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
  private static final Logger logger = LoggerFactory.getLogger(GenericExceptionMapper.class);

  @Override
  public Response toResponse(Exception exception) {
    logger.error("Internal Error: {}", exception.getMessage());

    ErrorResponse error = new ErrorResponse(
        "INTERNAL_ERROR",
        "An unexpected error occurred.",
        500);

    return Response.status(500).entity(error).build();
  }
}
