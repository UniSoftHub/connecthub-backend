package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.domain.exception.PageNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PageNotFoundExceptionMapper implements ExceptionMapper<PageNotFoundException> {

  private static final Logger logger = LoggerFactory.getLogger(PageNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(PageNotFoundException exception) {
    logger.warn("Page not found exception {}", exception.getMessage());

    ErrorResponse error = new ErrorResponse(
        "PAGE_NOT_FOUND_EXCEPTION",
        exception.getMessage(),
        400);

    return Response.status(400).entity(error).build();
  }
}
