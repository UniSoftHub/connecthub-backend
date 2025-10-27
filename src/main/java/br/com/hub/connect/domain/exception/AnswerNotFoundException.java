package br.com.hub.connect.domain.exception;

public class AnswerNotFoundException extends RuntimeException {

  public AnswerNotFoundException(Long id) {
    super("Answer not found with id: " + id);
  }

  public AnswerNotFoundException(String message) {
    super(message);
  }

  public AnswerNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
