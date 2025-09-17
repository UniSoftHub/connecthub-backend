package br.com.hub.connect.domain.exception;

public class EmailAlreadyExistsException extends RuntimeException {
  public EmailAlreadyExistsException(String email) {
    super("Email " + email);
  }
}
