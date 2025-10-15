package br.com.hub.connect.interfaces.rest.project;

import jakarta.ws.rs.PathParam;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import br.com.hub.connect.application.project.projectComment.service.ProjectCommentService;
import br.com.hub.connect.application.project.projectComment.dto.CreateProjectCommentDTO;
import br.com.hub.connect.application.project.projectComment.dto.UpdateProjectCommentDTO;
import br.com.hub.connect.application.project.projectComment.dto.ProjectCommentResponseDTO;

// Ideias de path para comments:
//@Path("/api/projects/{projectId}/comments")
//@Path("/api/project/comments")

@Path("/api/projects/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Project Comments", description = "Operations about project comments")

public class ProjectCommentResource {

  @Inject
  ProjectCommentService projectCommentService;

  @GET
  @Operation(summary = "List all comments", description = "Returns a list of comments with pagination.")
  @APIResponse(responseCode = "200", description = "List of comments returned successfully")
  public Response getAllComments(

      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,

      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    List<ProjectCommentResponseDTO> comments = projectCommentService.findAll(page, size);
    return Response.ok(comments).build();
  }

  @GET
  @Path("/project/{projectId}")
  @Operation(summary = "List all comments for a project", description = "Returns a list of comments for a specific project")
  @APIResponse(responseCode = "200", description = "List of comments returned successfully")
  public Response getCommentsByProjectId(
      @Parameter(description = "ID of the project", required = true) @PathParam("projectId") @NotNull Long projectId,
      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {
    List<ProjectCommentResponseDTO> comments = projectCommentService.findByProjectId(projectId, page, size);
    return Response.ok(comments).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Find comment by ID")
  @APIResponse(responseCode = "200", description = "Project comment found")
  @APIResponse(responseCode = "404", description = "Project comment not found")
  public Response getCommentById(
      @Parameter(description = "ID of the comment", required = true) @PathParam("id") @NotNull Long id) {

    ProjectCommentResponseDTO comment = projectCommentService.findById(id);
    return Response.ok(comment).build();
  }

  @POST
  @Operation(summary = "Create a new project comment")
  @APIResponse(responseCode = "201", description = "Project comment created successfully")
  @APIResponse(responseCode = "400", description = "Invalid input data")
  @APIResponse(responseCode = "409", description = "Conflict")
  @APIResponse(responseCode = "500", description = "Internal server error")
  public Response createComment(@Valid CreateProjectCommentDTO dto) {

    ProjectCommentResponseDTO createdComment = projectCommentService.create(dto);

    return Response.status(Response.Status.CREATED)
        .location(UriBuilder.fromPath("/api/projects/comments/{id}")
            .build(createdComment.id()))
        .entity(createdComment)
        .build();
  }

  @PATCH
  @Path("/{id}")
  @Operation(summary = "Update a project comment")
  @APIResponse(responseCode = "200", description = "Project comment updated successfully")
  @APIResponse(responseCode = "400", description = "Invalid input data")
  @APIResponse(responseCode = "404", description = "Project comment not found")
  @APIResponse(responseCode = "409", description = "Conflict")
  @APIResponse(responseCode = "500", description = "Internal server error")
  public Response updateComment(
      @Parameter(description = "ID of the comment to be updated", required = true) @PathParam("id") @NotNull Long id,
      @Valid UpdateProjectCommentDTO dto) {

    projectCommentService.update(id, dto);
    return Response.ok().build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete a project comment", description = "Removes a project comment by its ID (soft delete)")
  @APIResponse(responseCode = "204", description = "Project comment removed successfully")
  @APIResponse(responseCode = "404", description = "Project comment not found")
  public Response deleteComment(
      @Parameter(description = "ID of the comment to be deleted", required = true) @PathParam("id") @NotNull Long id) {

    projectCommentService.delete(id);
    return Response.noContent().build();
  }

  @GET
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the Project Comment service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok("Project Comment service is healthy").build();
  }

  @GET
  @Path("/count")
  @Operation(summary = "Count active project comments", description = "Returns the total number of active project comments")
  @APIResponse(responseCode = "200", description = "Total number of active project commentsreturned successfully")
  public Response countActiveComments() {
    long count = projectCommentService.count();
    return Response.ok(new CountResponse(count)).build();
  }

  public record CountResponse(Long count) {

  }
}
