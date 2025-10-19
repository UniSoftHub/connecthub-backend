package br.com.hub.connect.interfaces.rest.academic;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.hub.connect.application.academic.dto.topic.CreateTopicDTO;
import br.com.hub.connect.application.academic.dto.topic.UpdateTopicDTO;
import br.com.hub.connect.application.academic.dto.topic.TopicResponseDTO;
import br.com.hub.connect.application.academic.service.TopicService;
import br.com.hub.connect.domain.academic.enums.TopicStatus;
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

@Path("/api/topics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Topics", description = "Operations about topics")
public class TopicResource {

  @Inject
  TopicService topicService;

  @GET
  @Operation(summary = "List all active topics", description = "Returns a paged list of active topics")
  @APIResponse(responseCode = "200", description = "List of topics returned successfully")
  public Response getAllTopics(
      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,

      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size,

      @Parameter(description = "Filter by course ID") @QueryParam("courseId") Long courseId,

      @Parameter(description = "Filter by author ID") @QueryParam("authorId") Long authorId,

      @Parameter(description = "Filter by status") @QueryParam("status") TopicStatus status) {

    List<TopicResponseDTO> topics;

    if (courseId != null) {
      topics = topicService.findByCourse(courseId, page, size);
    } else if (authorId != null) {
      topics = topicService.findByAuthor(authorId, page, size);
    } else if (status != null) {
      topics = topicService.findByStatus(status, page, size);
    } else {
      topics = topicService.findAll(page, size);
    }

    return Response.ok(topics).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Find topic by ID")
  @APIResponse(responseCode = "200", description = "Topic found")
  @APIResponse(responseCode = "404", description = "Topic not found")
  public Response getTopicById(
      @Parameter(description = "ID of the topic", required = true) @PathParam("id") @NotNull Long id) {

    TopicResponseDTO topic = topicService.findById(id);
    return Response.ok(topic).build();
  }

  @POST
  @Operation(summary = "Create a new topic")
  @APIResponse(responseCode = "201", description = "Topic created successfully")
  @APIResponse(responseCode = "400", description = "Invalid data")
  @APIResponse(responseCode = "404", description = "Course or author not found")
  public Response createTopic(@Valid CreateTopicDTO dto) {

    TopicResponseDTO createdTopic = topicService.create(dto);

    return Response.status(Response.Status.CREATED)
        .entity(createdTopic)
        .location(UriBuilder.fromPath("/api/topics/{id}")
            .build(createdTopic.id()))
        .build();
  }

  @PATCH
  @Path("/{id}")
  @Operation(summary = "Update an existing topic")
  @APIResponse(responseCode = "200", description = "Topic updated successfully")
  @APIResponse(responseCode = "404", description = "Topic not found")
  public Response updateTopic(
      @Parameter(description = "ID of the topic", required = true) @PathParam("id") @NotNull Long id,
      @Valid UpdateTopicDTO dto) {

    TopicResponseDTO updatedTopic = topicService.update(id, dto);
    return Response.ok(updatedTopic).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete a topic", description = "Removes a topic by its ID (soft delete)")
  @APIResponse(responseCode = "204", description = "Topic removed successfully")
  @APIResponse(responseCode = "404", description = "Topic not found")
  public Response deleteTopic(
      @Parameter(description = "ID of the topic", required = true) @PathParam("id") @NotNull Long id) {

    topicService.delete(id);
    return Response.noContent().build();
  }

  @GET
  @Path("/course/{courseId}")
  @Operation(summary = "List topics by course", description = "Returns all topics for a specific course")
  @APIResponse(responseCode = "200", description = "List of topics returned successfully")
  @APIResponse(responseCode = "404", description = "Course not found")
  public Response getTopicsByCourse(
      @Parameter(description = "ID of the course", required = true) @PathParam("courseId") @NotNull Long courseId,
      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    List<TopicResponseDTO> topics = topicService.findByCourse(courseId, page, size);
    long count = topicService.countByCourse(courseId);

    return Response.ok(new TopicListResponse(topics, count)).build();
  }

  @GET
  @Path("/author/{authorId}")
  @Operation(summary = "List topics by author", description = "Returns all topics created by a specific author")
  @APIResponse(responseCode = "200", description = "List of topics returned successfully")
  @APIResponse(responseCode = "404", description = "Author not found")
  public Response getTopicsByAuthor(
      @Parameter(description = "ID of the author", required = true) @PathParam("authorId") @NotNull Long authorId,
      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    List<TopicResponseDTO> topics = topicService.findByAuthor(authorId, page, size);
    long count = topicService.countByAuthor(authorId);

    return Response.ok(new TopicListResponse(topics, count)).build();
  }

  public record TopicListResponse(List<TopicResponseDTO> topics, long count) {
  }

  @GET
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the Topic service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok("Topic service is healthy").build();
  }

  @GET
  @Path("/count")
  @Operation(summary = "Count active topics", description = "Returns the total number of active topics")
  @APIResponse(responseCode = "200", description = "Total number of active topics returned successfully")
  public Response countActiveTopics() {
    long count = topicService.count();
    return Response.ok(new CountResponse(count)).build();
  }

  public record CountResponse(long count) {
  }
}
