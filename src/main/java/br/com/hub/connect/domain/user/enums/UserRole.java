package br.com.hub.connect.domain.user.enums;

public enum UserRole {
  STUDENT("Aluno"),
  TEACHER("Professor"),
  COORDINATOR("Coordenador"),
  ADMIN("Administrador");

  private final String displayName;

  UserRole(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
