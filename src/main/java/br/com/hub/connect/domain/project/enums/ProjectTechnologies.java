package br.com.hub.connect.domain.project.enums;

public enum ProjectTechnologies {
  JAVA("Java"),
  JAVASCRIPT("JavaScript"),
  PYTHON("Python"),
  RUBY("Ruby"),
  PHP("PHP"),
  CSHARP("C#"),
  CPLUSPLUS("C++"),
  C("C"),
  SWIFT("Swift"),
  KOTLIN("Kotlin"),
  GO("Go"),
  TYPESCRIPT("TypeScript"),
  RUST("Rust"),
  HTML("HTML"),
  CSS("CSS"),
  SQL("SQL"),
  NOSQL("NoSQL"),
  DOCKER("Docker"),
  KUBERNETES("Kubernetes"),
  AWS("AWS"),
  AZURE("Azure"),
  GCP("GCP"),
  LINUX("Linux"),
  WINDOWS("Windows"),
  MACOS("macOS"),
  NODEJS("Node.js"),
  REACT("React"),
  ANGULAR("Angular"),
  VUE("Vue.js"),
  DJANGO("Django"),
  FLASK("Flask"),
  SPRING("Spring"),
  LARAVEL("Laravel"),
  RUBYONRAILS("Ruby on Rails"),
  ASPNET("ASP.NET"),
  EXPRESS("Express.js"),
  MONGODB("MongoDB"),
  POSTGRESQL("PostgreSQL"),
  MYSQL("MySQL"),
  SQLITE("SQLite"),
  REDIS("Redis"),
  GRAPHQL("GraphQL"),
  REST("REST"),
  GIT("Git");

  private final String displayName;

  ProjectTechnologies(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

}
