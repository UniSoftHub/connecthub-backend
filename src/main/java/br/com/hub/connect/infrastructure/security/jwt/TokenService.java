package br.com.hub.connect.infrastructure.security.jwt;

import java.util.HashSet;
import java.util.Set;

import br.com.hub.connect.domain.user.model.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {

  public String generateToken(User user) {
    Set<String> roles = new HashSet<>();
    roles.add(user.role.name());

    return Jwt.issuer("https://connecthub.com/issuer")
        .upn(user.email)
        .subject(user.id.toString())
        .groups(roles)
        .claim("userId", user.id)
        .claim("name", user.name)
        .claim("email", user.email)
        .claim("role", user.role.name())
        .expiresIn(3600) // 1 hora
        .sign();
  }

  public String generateRefreshToken(User user) {
    return Jwt.issuer("https://connecthub.com/issuer")
        .upn(user.email)
        .subject(user.id.toString())
        .claim("userId", user.id)
        .claim("type", "refresh")
        .expiresIn(604800) // 7 dias
        .sign();
  }
}
