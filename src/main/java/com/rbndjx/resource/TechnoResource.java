package com.rbndjx.resource;

import com.rbndjx.dto.SimpleTechnoDTO;
import com.rbndjx.dto.TechnoDTO;
import com.rbndjx.service.TechnoService;
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

@Path("/technologies")
@Tag(name = "Technologies", description = "Technology management endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TechnoResource {

    @Inject
    TechnoService technoService;

    @GET
    @Operation(summary = "Get all technologies")
    @APIResponse(responseCode = "200", description = "List of all technologies")
    public List<TechnoDTO> getAllTechnologies() {
        return technoService.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get technology by ID")
    @APIResponse(responseCode = "200", description = "Technology retrieved successfully")
    @APIResponse(responseCode = "404", description = "Technology not found")
    public TechnoDTO getTechnologyById(@PathParam("id") String id) {
        return technoService.getById(id);
    }

    @POST
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Create a new technology", description = "Requires authentication")
    @APIResponse(responseCode = "201", description = "Technology created successfully")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public Response createTechnology(SimpleTechnoDTO dto) {
        TechnoDTO created = technoService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Update a technology", description = "Requires authentication")
    @APIResponse(responseCode = "200", description = "Technology updated successfully")
    @APIResponse(responseCode = "404", description = "Technology not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public TechnoDTO updateTechnology(@PathParam("id") String id, SimpleTechnoDTO dto) {
        return technoService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Delete a technology", description = "Requires authentication")
    @APIResponse(responseCode = "204", description = "Technology deleted successfully")
    @APIResponse(responseCode = "404", description = "Technology not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public Response deleteTechnology(@PathParam("id") String id) {
        technoService.delete(id);
        return Response.noContent().build();
    }
}
