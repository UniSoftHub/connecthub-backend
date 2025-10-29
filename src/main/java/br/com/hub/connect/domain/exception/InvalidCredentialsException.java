package br.com.hub.connect.domain.exception;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invalid Credentials");
  }
}
