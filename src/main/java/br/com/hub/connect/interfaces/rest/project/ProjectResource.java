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
import br.com.hub.connect.application.project.project.service.ProjectService;
import br.com.hub.connect.application.project.project.dto.CreateProjectDTO;
import br.com.hub.connect.application.project.project.dto.UpdateProjectDTO;
import br.com.hub.connect.application.project.project.dto.ProjectResponseDTO;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;

@Path("/api/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Projects", description = "Operations about projects")
public class ProjectResource {

    @Inject
    ProjectService projectService;

    @GET
    @Operation(summary = "List all active projects", description = "Returns a paged list of active projects")
    @APIResponse(responseCode = "200", description = "List of projects returned successfully")
    public Response getAllProjects(
        @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,

        @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size

    ) {
        List<ProjectResponseDTO> projects = projectService.findAll(page, size);
        return Response.ok(projects).build();
    }


    @GET
    @Path("/{id}")
    @Operation(summary = "Find project by ID")
    @APIResponse(responseCode = "200", description = "Project found")
    @APIResponse(responseCode = "404", description = "Project not found")
    public Response getProjectById(
        @Parameter(description = "ID of the project", required = true) @PathParam("id") @NotNull Long id) {

    ProjectResponseDTO project = projectService.findById(id);
    return Response.ok(project).build();
  }

  @POST
  @Operation(summary = "Create a new project")
  @APIResponse(responseCode = "201", description = "Project created successfully")
  @APIResponse(responseCode = "400", description = "Invalid data")
  public Response createProject(@Valid CreateProjectDTO dto) {

    ProjectResponseDTO createdProject = projectService.create(dto);

    return Response.status(Response.Status.CREATED)
        .entity(createdProject)
        .location(UriBuilder.fromPath("/api/projects/{id}")
            .build(createdProject.id()))
        .build();
  }

  @PATCH
  @Path("/{id}")
  @Operation(summary = "Update an existing project")
  @APIResponse(responseCode = "200", description = "Project updated successfully")
  @APIResponse(responseCode = "404", description = "Project not found")
  @APIResponse(responseCode = "409", description = "Conflict")
  @APIResponse(responseCode = "500", description = "Internal server error")
  public Response updateProject(
      @Parameter(description = "ID of the project", required = true) @PathParam("id") @NotNull Long id,
      @Valid UpdateProjectDTO dto) {

    ProjectResponseDTO updatedProject = projectService.update(id, dto);
    return Response.ok(updatedProject).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete a project", description = "Removes a project by its ID (soft delete)")
  @APIResponse(responseCode = "204", description = "Project removed successfully")
  @APIResponse(responseCode = "404", description = "Project not found")
  public Response deleteProject(
    @Parameter(description = "ID of the project", required = true) @PathParam("id") @NotNull Long id) {

  projectService.delete(id);
  return Response.noContent().build();
  }

  @GET
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the Project service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok("Project service is healthy").build();
  }

  @GET
  @Path("/count")
  @Operation(summary = "Count active projects", description = "Returns the total number of active projects")
  @APIResponse(responseCode = "200", description = "Total number of active projects returned successfully")
  public Response countActiveProjects() {
    long count = projectService.count();
    return Response.ok(new CountResponse(count)).build();
  }

  public record CountResponse(long count) {
  

}

}
