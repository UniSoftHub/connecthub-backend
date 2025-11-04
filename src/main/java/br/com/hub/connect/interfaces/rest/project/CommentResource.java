package br.com.hub.connect.interfaces.rest.project;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.hub.connect.application.project.projectComment.dto.ProjectCommentResponseDTO;
import br.com.hub.connect.application.project.projectComment.dto.UpdateProjectCommentDTO;
import br.com.hub.connect.application.project.projectComment.service.ProjectCommentService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/comments") 
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Comments", description = "Global operations for all comments")
public class CommentResource {

    @Inject
    ProjectCommentService projectCommentService;

    @GET
    @Operation(summary = "List ALL comments (paginated)", description = "Returns a list of all comments in the system, with pagination.")
    @APIResponse(responseCode = "200", description = "List of comments returned successfully")
    public Response getAllComments(
            @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {
        
        List<ProjectCommentResponseDTO> comments = projectCommentService.findAllGlobal(page, size);
        return Response.ok(comments).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Find comment by its Global ID")
    @APIResponse(responseCode = "200", description = "Project comment found")
    @APIResponse(responseCode = "404", description = "Project comment not found")
    public Response getCommentById(
            @Parameter(description = "Global ID of the comment", required = true) @PathParam("id") @NotNull Long id) {

        ProjectCommentResponseDTO comment = projectCommentService.findByIdGlobal(id);
        return Response.ok(comment).build();
    }

    @PATCH
    @Path("/{id}")
    @Operation(summary = "Update a project comment by its Global ID")
    @APIResponse(responseCode = "200", description = "Project comment updated successfully")
    @APIResponse(responseCode = "404", description = "Project comment not found")
    public Response updateComment(
            @Parameter(description = "Global ID of the comment to be updated", required = true) @PathParam("id") @NotNull Long id,
            @Valid UpdateProjectCommentDTO dto) {

        ProjectCommentResponseDTO updatedComment = projectCommentService.updateGlobal(id, dto);
        return Response.ok(updatedComment).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a project comment by its Global ID", description = "Removes a project comment by its ID (soft delete)")
    @APIResponse(responseCode = "204", description = "Project comment removed successfully")
    @APIResponse(responseCode = "404", description = "Project comment not found")
    public Response deleteComment(
            @Parameter(description = "Global ID of the comment to be deleted", required = true) @PathParam("id") @NotNull Long id) {

        projectCommentService.deleteGlobal(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Count ALL active project comments", description = "Returns the total number of active project comments in the system")
    @APIResponse(responseCode = "200", description = "Total number of active project comments returned successfully")
    public Response countActiveComments() {
        long count = projectCommentService.countAllGlobal();
        return Response.ok(new CountResponse(count)).build();
    }

    public record CountResponse(Long count) { }
}