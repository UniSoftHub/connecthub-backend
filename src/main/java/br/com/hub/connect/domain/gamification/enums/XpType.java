package br.com.hub.connect.domain.gamification.enums;

public enum XpType {
  TOPIC_CREATED("Tópico criado", 10),
  ANSWER_POSTED("Resposta postada", 15),
  BEST_ANSWER("Melhor resposta", 25),
  PROJECT_PUBLISHED("Projeto publicado", 20);

  private final String description;
  private final int defaultAmount;

  XpType(String description, int defaultAmount) {
    this.description = description;
    this.defaultAmount = defaultAmount;
  }

  public String getDescription() {
    return description;
  }

  public int getDefaultAmount() {
    return defaultAmount;
  }

}
