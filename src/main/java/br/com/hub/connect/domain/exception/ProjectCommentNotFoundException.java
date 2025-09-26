package br.com.hub.connect.domain.exception;

public class ProjectCommentNotFoundException extends RuntimeException {
  public ProjectCommentNotFoundException(Long id) {
    super("Project comment not found with ID: " + id);
  }
}
