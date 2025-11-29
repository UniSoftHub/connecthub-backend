package br.com.hub.connect.infrastructure.security.jwt;

import br.com.hub.connect.application.user.dto.UserResponseDTO;
import br.com.hub.connect.domain.exception.EmailAlreadyExistsException;
import br.com.hub.connect.domain.exception.InvalidCredentialsException;
import br.com.hub.connect.domain.user.enums.UserRole;
import br.com.hub.connect.domain.user.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

  @Inject
  TokenService tokenService;

  @POST
  @Path("/login")
  @Transactional
  public Response login(LoginRequest request) {
    User user = User.find("email = ?1 and isActive = true", request.email)
        .firstResult();

    if (user == null) {
      throw new InvalidCredentialsException();
    }

    if (!BcryptUtil.matches(request.password, user.password)) {
      throw new InvalidCredentialsException();
    }

    String token = tokenService.generateToken(user);
    String refreshToken = tokenService.generateRefreshToken(user);

    return Response.ok(new LoginResponse(token, refreshToken, user)).build();
  }

  @POST
  @Path("/register")
  @Transactional
  public Response register(RegisterRequest request) {
    if (User.existsByEmailActive(request.email)) {
      throw new EmailAlreadyExistsException(request.email);
    }

    User user = new User();
    user.name = request.name;
    user.email = request.email;
    user.password = BcryptUtil.bcryptHash(request.password);
    user.role = request.role != null ? request.role : UserRole.STUDENT;
    user.isActive = true;

    user.persist();

    String token = tokenService.generateToken(user);
    String refreshToken = tokenService.generateRefreshToken(user);

    return Response.status(Response.Status.CREATED)
        .entity(new LoginResponse(token, refreshToken, user))
        .build();
  }

  public static class LoginRequest {
    public String email;
    public String password;
  }

  public static class RegisterRequest {
    public String name;
    public String email;
    public String password;
    public UserRole role;
  }

  public static class LoginResponse {
    public String token;
    public String refreshToken;
    public UserResponseDTO user;

    public LoginResponse(String token, String refreshToken, User user) {
      this.token = token;
      this.refreshToken = refreshToken;
      this.user = toResponseDTO(user);
    }
  }

  private static UserResponseDTO toResponseDTO(User user) {
    return new UserResponseDTO(
        user.id,
        user.name,
        user.email,
        user.role,
        user.enrollmentId,
        user.CPF,
        user.phone,
        user.xp,
        user.level,
        user.avatarUrl,
        user.isActive,
        user.createdAt);
  }
}
