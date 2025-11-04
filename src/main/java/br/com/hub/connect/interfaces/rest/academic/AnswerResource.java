package br.com.hub.connect.interfaces.rest.academic;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.hub.connect.application.academic.dto.answer.CreateAnswerDTO;
import br.com.hub.connect.application.academic.dto.answer.UpdateAnswerDTO;
import br.com.hub.connect.application.academic.dto.answer.AnswerResponseDTO;
import br.com.hub.connect.application.academic.dto.answer.AnswerListResponseDTO;
import br.com.hub.connect.application.academic.service.AnswerService;
import br.com.hub.connect.application.utils.CountResponse;
import br.com.hub.connect.domain.exception.PageNotFoundException;
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

@Path("/api/answers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Answers", description = "Operations about answers")
public class AnswerResource {

  @Inject
  AnswerService answerService;

  @GET
  @Operation(summary = "List all active answers", description = "Returns a paged list of active answers")
  @APIResponse(responseCode = "200", description = "List of answers returned successfully")
  public Response getAllAnswers(
      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("1") int page,

      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size,

      @Parameter(description = "Filter by topic ID") @QueryParam("topicId") Long topicId,

      @Parameter(description = "Filter by author ID") @QueryParam("authorId") Long authorId) {

    if (page < 1) {
      throw new PageNotFoundException();
    }
    int pageIndex = page - 1;

    List<AnswerResponseDTO> answers = answerService.findWithFilters(topicId, authorId, pageIndex, size);

    var totalCount = getActiveAnswersCount();
    int totalPages = (int) Math.ceil((double) totalCount / size);

    return Response.ok(new AnswerListResponseDTO(totalPages, answers)).build();
  }

  private long getActiveAnswersCount() {
    return answerService.count();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Find answer by ID")
  @APIResponse(responseCode = "200", description = "Answer found")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response getAnswerById(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.findById(id);
    return Response.ok(answer).build();
  }

  @POST
  @Operation(summary = "Create a new answer")
  @APIResponse(responseCode = "201", description = "Answer created successfully")
  @APIResponse(responseCode = "400", description = "Invalid data")
  @APIResponse(responseCode = "404", description = "Topic or author not found")
  public Response createAnswer(@Valid CreateAnswerDTO dto) {

    AnswerResponseDTO createdAnswer = answerService.create(dto);

    return Response.status(Response.Status.CREATED)
        .entity(createdAnswer)
        .location(UriBuilder.fromPath("/api/answers/{id}")
            .build(createdAnswer.id()))
        .build();
  }

  @PATCH
  @Path("/{id}")
  @Operation(summary = "Update an existing answer")
  @APIResponse(responseCode = "200", description = "Answer updated successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response updateAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id,
      @Valid UpdateAnswerDTO dto) {

    AnswerResponseDTO updatedAnswer = answerService.update(id, dto);
    return Response.ok(updatedAnswer).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete an answer", description = "Removes an answer by its ID (soft delete)")
  @APIResponse(responseCode = "204", description = "Answer removed successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response deleteAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    answerService.delete(id);
    return Response.noContent().build();
  }

  @POST
  @Path("/{id}/like")
  @Operation(summary = "Like an answer")
  @APIResponse(responseCode = "200", description = "Answer liked successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response likeAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.like(id);
    return Response.ok(answer).build();
  }

  @POST
  @Path("/{id}/unlike")
  @Operation(summary = "Remove like from an answer")
  @APIResponse(responseCode = "200", description = "Like removed successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response unlikeAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.unlike(id);
    return Response.ok(answer).build();
  }

  @POST
  @Path("/{id}/dislike")
  @Operation(summary = "Dislike an answer")
  @APIResponse(responseCode = "200", description = "Answer disliked successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response dislikeAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.dislike(id);
    return Response.ok(answer).build();
  }

  @POST
  @Path("/{id}/undislike")
  @Operation(summary = "Remove dislike from an answer")
  @APIResponse(responseCode = "200", description = "Dislike removed successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response undislikeAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.undislike(id);
    return Response.ok(answer).build();
  }

  @POST
  @Path("/{id}/mark-solution")
  @Operation(summary = "Mark answer as solution")
  @APIResponse(responseCode = "200", description = "Answer marked as solution successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response markAsSolution(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.markAsSolution(id);
    return Response.ok(answer).build();
  }

  @GET
  @Path("/topic/{topicId}/count")
  @Operation(summary = "Count answers by topic")
  @APIResponse(responseCode = "200", description = "Count returned successfully")
  public Response countByTopic(
      @Parameter(description = "ID of the topic", required = true) @PathParam("topicId") @NotNull Long topicId) {

    long count = answerService.countByTopic(topicId);
    return Response.ok(new CountResponse(count)).build();
  }

  @GET
  @Path("/author/{authorId}/count")
  @Operation(summary = "Count answers by author")
  @APIResponse(responseCode = "200", description = "Count returned successfully")
  public Response countByAuthor(
      @Parameter(description = "ID of the author", required = true) @PathParam("authorId") @NotNull Long authorId) {

    long count = answerService.countByAuthor(authorId);
    return Response.ok(new CountResponse(count)).build();
  }

  @GET
  @Path("/author/{authorId}/solutions/count")
  @Operation(summary = "Count solutions by author")
  @APIResponse(responseCode = "200", description = "Count returned successfully")
  public Response countSolutionsByAuthor(
      @Parameter(description = "ID of the author", required = true) @PathParam("authorId") @NotNull Long authorId) {

    long count = answerService.countSolutionsByAuthor(authorId);
    return Response.ok(new CountResponse(count)).build();
  }

  @GET
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the Answer service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok("Answer service is healthy").build();
  }

  @GET
  @Path("/count")
  @Operation(summary = "Count active answers", description = "Returns the total number of active answers")
  @APIResponse(responseCode = "200", description = "Total number of active answers returned successfully")
  public Response countActiveAnswers() {
    long count = answerService.count();
    return Response.ok(new CountResponse(count)).build();
  }

}
