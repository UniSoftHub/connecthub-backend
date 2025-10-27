package br.com.hub.connect.domain.exception;

public class TopicNotFoundException extends RuntimeException {

  public TopicNotFoundException(Long id) {
    super("Topic not found with id: " + id);
  }

  public TopicNotFoundException(String message) {
    super(message);
  }

  public TopicNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
