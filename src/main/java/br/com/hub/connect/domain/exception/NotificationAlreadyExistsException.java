package br.com.hub.connect.domain.exception;

public class NotificationAlreadyExistsException extends RuntimeException {
  public NotificationAlreadyExistsException(String title) {
    super("Notification with title '" + title + "' already exists.");
  }

}
