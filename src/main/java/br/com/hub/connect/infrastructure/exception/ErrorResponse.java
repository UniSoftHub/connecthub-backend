package br.com.hub.connect.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    String code,
    String message,
    int status,
    LocalDateTime timestamp,
    List<String> details) {
  public ErrorResponse(String code, String message, int status) {
    this(code, message, status, LocalDateTime.now(), null);
  }

  public ErrorResponse(String code, String message, int status, List<String> details) {
    this(code, message, status, LocalDateTime.now(), details);
  }
}
