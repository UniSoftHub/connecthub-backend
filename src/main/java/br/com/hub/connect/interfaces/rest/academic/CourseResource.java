package br.com.hub.connect.interfaces.rest.academic;

import br.com.hub.connect.application.academic.dto.course.CourseResponseDTO;
import br.com.hub.connect.application.academic.dto.course.CreateCourseDTO;
import br.com.hub.connect.application.academic.dto.course.UpdateCourseDTO;
import br.com.hub.connect.application.academic.service.CourseService;
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
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/courses")
@Produces({ "application/json" })
@Consumes({ "application/json" })
@Tag(name = "Courses", description = "Operations about courses")
public class CourseResource {
  @Inject
  CourseService courseService;

  @GET
  @Operation(summary = "List all courses", description = "Returns a paged list of active courses")
  @APIResponse(responseCode = "200", description = "List of courses returned successfully")
  public Response getAllCourses(
      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {
    List<CourseResponseDTO> courses = this.courseService.findAll(page, size);
    return Response.ok(courses).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Find course by ID")
  @APIResponses({ @APIResponse(responseCode = "200", description = "Course found"),
      @APIResponse(responseCode = "404", description = "Course not found") })
  public Response getCourseById(
      @Parameter(description = "ID of the course", required = true) @PathParam("id") @NotNull Long id) {
    CourseResponseDTO course = this.courseService.findById(id);
    return Response.ok(course).build();
  }

  @POST
  @Operation(summary = "Create a new course")
  @APIResponses({ @APIResponse(responseCode = "201", description = "Course created successfully"),
      @APIResponse(responseCode = "400", description = "Invalid data") })
  public Response createCourse(@Valid CreateCourseDTO dto) {
    CourseResponseDTO created = this.courseService.create(dto);
    return Response.created(UriBuilder.fromPath("/api/courses/{id}").build(new Object[] { created.id() }))
        .entity(created).build();
  }

  @PATCH
  @Path("/{id}")
  @Operation(summary = "Update a course")
  @APIResponses({ @APIResponse(responseCode = "200", description = "Course updated successfully"),
      @APIResponse(responseCode = "404", description = "Course not found") })
  public Response updateCourse(
      @Parameter(description = "ID of the course", required = true) @PathParam("id") @NotNull Long id,
      @Valid UpdateCourseDTO dto) {
    CourseResponseDTO updated = this.courseService.update(id, dto);
    return Response.ok(updated).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete a course", description = "Soft delete by ID")
  @APIResponses({ @APIResponse(responseCode = "204", description = "Course deleted successfully"),
      @APIResponse(responseCode = "404", description = "Course not found") })
  public Response deleteCourse(
      @Parameter(description = "ID of the course", required = true) @PathParam("id") @NotNull Long id) {
    this.courseService.delete(id);
    return Response.noContent().build();
  }

  @GET
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the Course service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok("Course service is healthy").build();
  }

  @GET
  @Path("/count")
  @Operation(summary = "Count active courses", description = "Returns the total number of active courses")
  @APIResponse(responseCode = "200", description = "Total number of active courses returned successfully")
  public Response countCourses() {
    long count = this.courseService.count();
    return Response.ok(new CountResponse(count)).build();
  }

  public static record CountResponse(long count) {
  }
}
