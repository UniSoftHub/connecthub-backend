package br.com.hub.connect.interfaces.rest.communication;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.hub.connect.application.communication.dto.CreateNotificationDTO;
import br.com.hub.connect.application.communication.dto.NotificationListResponseDTO;
import br.com.hub.connect.application.communication.dto.NotificationResponseDTO;
import br.com.hub.connect.application.communication.service.NotificationService;
import br.com.hub.connect.application.utils.ApiResponse;
import br.com.hub.connect.application.utils.CountResponse;
import br.com.hub.connect.domain.exception.PageNotFoundException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/api/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Notifications", description = "Operations about notifications")
public class NotificationResource {

  @Inject
  NotificationService notificationService;

  @GET
  @RolesAllowed({ "ADMIN" })
  @Operation(summary = "List all notifications", description = "Returns a list of all notifications")
  @APIResponse(responseCode = "200", description = "List of notifications returned successfully")
  public Response getAllNotifications(
      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    if (page < 1) {
      throw new PageNotFoundException();
    }
    int pageIndex = page - 1;

    List<NotificationResponseDTO> notifications = notificationService.findAll(pageIndex, size);

    var totalCount = getActiveNotificationCount();
    int totalPages = (int) Math.ceil((double) totalCount / size);

    NotificationListResponseDTO listResponse = new NotificationListResponseDTO(totalPages, notifications);

    return Response.ok(
        ApiResponse.success("Notifications retrieved sucessfully", listResponse)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN" })
  @Path("/{id}")
  @Operation(summary = "Find notification by ID", description = "Returns a notification by its ID")
  @APIResponse(responseCode = "200", description = "Notification found successfully")
  public Response getNotificationById(
      @Parameter(description = "ID of the notification to be retrieved") @PathParam("id") Long id) {

    NotificationResponseDTO notification = notificationService.findById(id);

    return Response.ok(
        ApiResponse.success("Notification found", notification)).build();
  }

  @POST
  @RolesAllowed({ "ADMIN" })
  @Operation(summary = "Send a new notification", description = "Creates and sends a new notification")
  @APIResponse(responseCode = "201", description = "Notification created and sent successfully")
  @APIResponse(responseCode = "400", description = "Invalid input data")
  public Response createNotification(@Valid CreateNotificationDTO dto) {

    NotificationResponseDTO createdNotification = notificationService.create(dto);

    return Response.status(Response.Status.CREATED)
        .entity(ApiResponse.success("Notification created successfully", createdNotification))
        .location(UriBuilder.fromPath("/api/notifications/{id}")
            .build(createdNotification.id()))
        .build();

  }

  @DELETE
  @RolesAllowed({ "ADMIN" })
  @Path("/{id}")
  @Operation(summary = "Delete a notification", description = "Deletes a notification by its ID (soft delete)")
  @APIResponse(responseCode = "200", description = "Notification deleted successfully")
  public Response deleteNotification(
      @Parameter(description = "ID of the notification to be deleted") @PathParam("id") Long id) {

    notificationService.delete(id);
    return Response.ok(
        ApiResponse.success("Notification deleted sucessfully")).build();
  }

  @GET
  @RolesAllowed({ "ADMIN" })
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the Notification service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response healthCheck() {
    return Response.ok(
        ApiResponse.success("Notification service is healthy")).build();
  }

  @GET
  @RolesAllowed({ "ADMIN" })
  @Path("/count")
  @Operation(summary = "Count notifications", description = "Returns the total number of notifications")
  @APIResponse(responseCode = "200", description = "Total number of notifications returned successfully")
  public Response countNotifications() {
    long count = getActiveNotificationCount();
    CountResponse countResponse = new CountResponse(count);

    return Response.ok(
        ApiResponse.success("Active notifications count retrieved", countResponse)).build();
  }

  private long getActiveNotificationCount() {
    return notificationService.count();
  }
}
