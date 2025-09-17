package br.com.hub.connect.infrastructure.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    ErrorResponse error = new ErrorResponse(
        "VALIDATION_ERROR",
        "Invalid Data",
        400,
        errors);

    return Response.status(400).entity(error).build();
  }
}
