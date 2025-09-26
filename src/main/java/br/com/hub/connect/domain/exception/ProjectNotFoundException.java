package br.com.hub.connect.domain.exception;

public class ProjectNotFoundException extends RuntimeException {
  public ProjectNotFoundException(Long id) {
    super("Project not found with ID: " + id);
  }
}
