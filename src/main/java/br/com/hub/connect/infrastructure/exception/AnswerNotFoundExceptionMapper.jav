package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.domain.exception.AnswerNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AnswerNotFoundExceptionMapper implements ExceptionMapper<AnswerNotFoundException> {

  private static final Logger logger = LoggerFactory.getLogger(AnswerNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(AnswerNotFoundException exception) {
    logger.warn("Answer not found: {}", exception.getMessage());
    
    ErrorResponse error = new ErrorResponse(
        "ANSWER_NOT_FOUND",
        exception.getMessage(),
        404);
    
    return Response.status(404).entity(error).build();
  }
}
