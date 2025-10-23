package br.com.hub.connect.domain.exception;

public class CodeAlreadyExistsException extends RuntimeException {

  public CodeAlreadyExistsException(String code) {
    super("A course with code '" + code + "' already exists");
  }

  public CodeAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
