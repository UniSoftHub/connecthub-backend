package br.com.hub.connect.domain.communication.enums;

public enum NotificationType {
  TOPIC_ANSWERED("Seu tópico recebeu uma nova resposta"),
  BEST_ANSWER_SELECTED("Sua resposta foi selecionada como a melhor resposta"),
  COURSE_ENROLLED("Você se inscreveu em um novo curso"),
  PROJECT_PUBLISHED("Um novo projeto foi publicado"),
  BADGE_EARNED("Você ganhou uma nova medalha"),
  LEVEL_UP("Parabéns! Você subiu de nível");

  private final String description;

  NotificationType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
