package com.rbndjx.resource;

import com.rbndjx.domain.TimelineEventType;
import com.rbndjx.dto.TimelineEventDTO;
import com.rbndjx.service.TimelineEventService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/timeline-events")
@Tag(name = "Timeline Events", description = "Timeline event management endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TimelineEventResource {

    @Inject
    TimelineEventService timelineEventService;

    @GET
    @Operation(summary = "Get all timeline events ordered by timestamp")
    @APIResponse(responseCode = "200", description = "List of all timeline events")
    public List<TimelineEventDTO> getAllEvents() {
        return timelineEventService.listAll();
    }

    @GET
    @Path("/type/{type}")
    @Operation(summary = "Get timeline events by type")
    @APIResponse(responseCode = "200", description = "List of timeline events by type")
    public List<TimelineEventDTO> getEventsByType(@PathParam("type") TimelineEventType type) {
        return timelineEventService.listByType(type);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get timeline event by ID")
    @APIResponse(responseCode = "200", description = "Timeline event retrieved successfully")
    @APIResponse(responseCode = "404", description = "Timeline event not found")
    public TimelineEventDTO getEventById(@PathParam("id") String id) {
        return timelineEventService.getById(id);
    }

    @POST
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Create a new timeline event", description = "Requires authentication")
    @APIResponse(responseCode = "201", description = "Timeline event created successfully")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public Response createEvent(TimelineEventDTO dto) {
        TimelineEventDTO created = timelineEventService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Update a timeline event", description = "Requires authentication")
    @APIResponse(responseCode = "200", description = "Timeline event updated successfully")
    @APIResponse(responseCode = "404", description = "Timeline event not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public TimelineEventDTO updateEvent(@PathParam("id") String id, TimelineEventDTO dto) {
        return timelineEventService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Delete a timeline event", description = "Requires authentication")
    @APIResponse(responseCode = "204", description = "Timeline event deleted successfully")
    @APIResponse(responseCode = "404", description = "Timeline event not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public Response deleteEvent(@PathParam("id") String id) {
        timelineEventService.delete(id);
        return Response.noContent().build();
    }
}
