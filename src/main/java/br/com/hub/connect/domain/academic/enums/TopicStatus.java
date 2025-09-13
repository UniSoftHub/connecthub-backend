package br.com.hub.connect.domain.academic.enums;

public enum TopicStatus {
  NOT_ANSWERED("Não Respondido"),
  NOT_SOLVED("Não Solucionado"),
  SOLVED("Solucionado"),
  CLOSED("Fechado");

  private final String displayName;

  TopicStatus(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

}
