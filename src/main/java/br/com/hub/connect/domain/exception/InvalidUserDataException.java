package br.com.hub.connect.domain.exception;

public class InvalidUserDataException extends RuntimeException {
  public InvalidUserDataException(String message) {
    super(message);
  }
}
