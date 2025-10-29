package br.com.hub.connect.interfaces.rest.user;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.hub.connect.application.user.dto.CreateUserDTO;
import br.com.hub.connect.application.user.dto.UpdateUserDTO;
import br.com.hub.connect.application.user.dto.UserListResponseDTO;
import br.com.hub.connect.application.user.dto.UserResponseDTO;
import br.com.hub.connect.application.user.service.UserService;
import br.com.hub.connect.application.utils.CountResponse;
import br.com.hub.connect.domain.exception.PageNotFoundException;
import br.com.hub.connect.domain.user.enums.UserRole;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "Operations about users")
public class UserResource {

  @Inject
  UserService userService;

  @GET
  @Operation(summary = "List all active users", description = "Returns a paged list of active users")
  @APIResponse(responseCode = "200", description = "List of users returned successfully")
  public Response getAllUsers(
      @Parameter(description = "Page number (default: 1)") @QueryParam("page") @DefaultValue("1") int page,

      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size,

      @Parameter(description = "Filter by role") @QueryParam("role") UserRole role) {

    if (page < 1) {
      throw new PageNotFoundException();
    }
    int pageIndex = page - 1;

    List<UserResponseDTO> users = (role != null)
        ? userService.findByRole(role, pageIndex, size)
        : userService.findAll(pageIndex, size);

    var totalCount = getActiveUsersCount();
    int totalPages = (int) Math.ceil((double) totalCount / size);

    return Response.ok(new UserListResponseDTO(totalPages, users)).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Find user by ID")
  @APIResponse(responseCode = "200", description = "User found")
  @APIResponse(responseCode = "404", description = "User not found")
  public Response getUserById(
      @Parameter(description = "ID of the user", required = true) @PathParam("id") @NotNull Long id) {

    UserResponseDTO user = userService.findById(id);
    return Response.ok(user).build();
  }

  @POST
  @Operation(summary = "Create a new user")
  @APIResponse(responseCode = "201", description = "User created successfully")
  @APIResponse(responseCode = "400", description = "Invalid data")
  @APIResponse(responseCode = "409", description = "Email already exists")
  public Response createUser(@Valid CreateUserDTO dto) {

    UserResponseDTO createdUser = userService.create(dto);

    return Response.status(Response.Status.CREATED)
        .entity(createdUser)
        .location(UriBuilder.fromPath("/api/users/{id}")
            .build(createdUser.id()))
        .build();
  }

  @PATCH
  @Path("/{id}")
  @Operation(summary = "Update an existing user")
  @APIResponse(responseCode = "200", description = "User updated successfully")
  @APIResponse(responseCode = "404", description = "User not found")
  @APIResponse(responseCode = "409", description = "Email already exists")
  public Response updateUser(
      @Parameter(description = "ID of the user", required = true) @PathParam("id") @NotNull Long id,
      @Valid UpdateUserDTO dto) {

    UserResponseDTO updatedUser = userService.update(id, dto);
    return Response.ok(updatedUser).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete a user", description = "Removes a user by its ID (soft delete)")
  @APIResponse(responseCode = "204", description = "User removed successfully")
  @APIResponse(responseCode = "404", description = "User not found")
  public Response deleteUser(
      @Parameter(description = "ID of the user", required = true) @PathParam("id") @NotNull Long id) {

    userService.delete(id);
    return Response.noContent().build();
  }

  @POST
  @Path("/{id}/experience")
  @Operation(summary = "Add experience to user")
  @APIResponse(responseCode = "200", description = "Experience added successfully")
  @APIResponse(responseCode = "404", description = "User not found")
  public Response addExperience(
      @Parameter(description = "ID of the user", required = true) @PathParam("id") @NotNull Long id,

      @Parameter(description = "Experience points (default: 10)") @QueryParam("points") @DefaultValue("10") int points,

      @Parameter(description = "Experience reason") @QueryParam("reason") @DefaultValue("General activity") String reason) {

    UserResponseDTO updatedUser = userService.addExperience(id, points, reason);
    return Response.ok(updatedUser).build();
  }

  @GET
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the User service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok("User service is healthy").build();
  }

  @GET
  @Path("/count")
  @Operation(summary = "Count active users", description = "Returns the total number of active users")
  @APIResponse(responseCode = "200", description = "Total number of active users returned successfully")
  public Response countActiveUsers() {
    long count = getActiveUsersCount();
    return Response.ok(new CountResponse(count)).build();
  }

  private long getActiveUsersCount() {
    return userService.count();
  }
}
