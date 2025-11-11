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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import br.com.hub.connect.application.project.projectComment.service.ProjectCommentService;
import br.com.hub.connect.domain.exception.PageNotFoundException;
import br.com.hub.connect.application.project.projectComment.dto.CreateProjectCommentDTO;
import br.com.hub.connect.application.project.projectComment.dto.UpdateProjectCommentDTO;
import br.com.hub.connect.application.project.projectComment.dto.ProjectCommentResponseDTO;
import br.com.hub.connect.application.project.projectComment.dto.ProjectCommentListResponseDTO;

@Path("/api/projects/{projectId}/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Project Comments", description = "Operations about project comments")

public class ProjectCommentResource {

  @Inject
  ProjectCommentService projectCommentService;

  @Context
  UriInfo uriInfo;

  @GET
  @Operation(summary = "List all comments for a project", description = "Returns a list of comments for a specific project")
  @APIResponse(responseCode = "200", description = "List of comments returned successfully")
  public Response getCommentsByProjectId(
      @Parameter(description = "ID of the project", required = true) @PathParam("projectId") @NotNull Long projectId,
      @Parameter(description = "Page number (default: 1)") @QueryParam("page") @DefaultValue("1") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    if (page < 1) {
      throw new PageNotFoundException();
    }
    int pageIndex = page - 1;

    long totalCount = projectCommentService.countByProjectId(projectId);

    int totalPages = (int) Math.ceil((double) totalCount / size);
    if (totalPages == 0 && totalCount > 0) {
      totalPages = 1;
    }

    List<ProjectCommentResponseDTO> comments = projectCommentService.findByProjectId(projectId, pageIndex, size);

    return Response.ok(new ProjectCommentListResponseDTO(totalPages, comments)).build();
  }

  @GET
  @Path("/{commentId}")
  @Operation(summary = "Find comment by ID per project", description = "Returns a specific comment by its ID for a given project")
  @APIResponse(responseCode = "200", description = "Project comment found")
  @APIResponse(responseCode = "404", description = "Project comment not found")
  public Response getCommentById(
      @Parameter(description = "ID of the project", required = true) @PathParam("projectId") @NotNull Long projectId,
      @Parameter(description = "ID of the comment", required = true) @PathParam("commentId") @NotNull Long commentId) {

    ProjectCommentResponseDTO comment = projectCommentService.findCommentByIdAndProjectId(projectId, commentId);
    return Response.ok(comment).build();
  }

  @POST
  @Operation(summary = "Create a new project comment")
  @APIResponse(responseCode = "201", description = "Project comment created successfully")
  @APIResponse(responseCode = "400", description = "Invalid input data")
  public Response createComment(

      @Parameter(description = "ID of the project", required = true) @PathParam("projectId") @NotNull Long projectId,
      @Valid CreateProjectCommentDTO dto) {

    ProjectCommentResponseDTO createdComment = projectCommentService.create(projectId, dto);

    return Response.status(Response.Status.CREATED)
        .location(uriInfo.getAbsolutePathBuilder()
            .path(createdComment.id().toString())
            .build())
        .entity(createdComment)
        .build();
  }

  @PATCH
  @Path("/{commentId}")
  @Operation(summary = "Update a project comment")
  @APIResponse(responseCode = "200", description = "Project comment updated successfully")
  @APIResponse(responseCode = "400", description = "Invalid input data")
  @APIResponse(responseCode = "404", description = "Project comment not found")
  public Response updateComment(
      @Parameter(description = "ID of the project", required = true) @PathParam("projectId") @NotNull Long projectId,
      @Parameter(description = "ID of the comment to be updated", required = true) @PathParam("commentId") @NotNull Long commentId,
      @Valid UpdateProjectCommentDTO dto) {

    ProjectCommentResponseDTO updatedComment = projectCommentService.update(projectId, commentId, dto);
    return Response.ok(updatedComment).build();
  }

  @DELETE
  @Path("/{commentId}")
  @Operation(summary = "Delete a project comment", description = "Removes a project comment by its ID (soft delete)")
  @APIResponse(responseCode = "204", description = "Project comment removed successfully")
  @APIResponse(responseCode = "404", description = "Project comment not found")
  public Response deleteComment(
      @Parameter(description = "ID of the project", required = true) @PathParam("projectId") @NotNull Long projectId,
      @Parameter(description = "ID of the comment to be deleted", required = true) @PathParam("commentId") @NotNull Long commentId) {

    projectCommentService.delete(projectId, commentId);
    return Response.noContent().build();
  }

  @GET
  @Path("/health")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Health check", description = "Returns the health status of the Project Comment service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok("Project Comment service is healthy").build();
  }

  @GET
  @Path("/count")
  @Operation(summary = "Count active project comments per project", description = "Returns the total number of active project comments for a given project")
  @APIResponse(responseCode = "200", description = "Total number of active project comments per project returned successfully")
  public Response countActiveComments(
      @Parameter(description = "ID of the project", required = true) @PathParam("projectId") @NotNull Long projectId) {
    long count = projectCommentService.countByProjectId(projectId);
    return Response.ok(new CountResponse(count)).build();
  }

  public record CountResponse(Long count) {

  }
}
