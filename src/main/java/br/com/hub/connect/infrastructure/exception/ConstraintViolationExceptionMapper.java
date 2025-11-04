package br.com.hub.connect.infrastructure.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.application.utils.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
  private static final Logger logger = LoggerFactory.getLogger(ConstraintViolationExceptionMapper.class);

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    logger.warn("Validation Error: {}", exception.getMessage());

    List<String> errors = exception.getConstraintViolations()
        .stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .collect(Collectors.toList());

    ErrorResponse errorDetails = new ErrorResponse(
        "VALIDATION_ERROR",
        "Invalid Data",
        400,
        errors);

    ApiResponse<ErrorResponse> response = ApiResponse.error(
        "Validation Error",
        errorDetails);

    return Response.status(400).entity(response).build();
  }
}
