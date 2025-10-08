package com.rbndjx.resource;

import com.rbndjx.dto.*;
import com.rbndjx.service.ProjectService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.util.List;

@Path("/projects")
@Tag(name = "Projects", description = "Project management endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectResource {

    @Inject
    ProjectService projectService;

    @GET
    @Operation(summary = "Get all projects", description = "List of all projects retrieved successfully")
    @APIResponse(responseCode = "200", description = "List of all projects")
    public List<ProjectDTO> getAllProjects() {
        return projectService.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a project by ID")
    @APIResponse(responseCode = "200", description = "Project retrieved successfully")
    @APIResponse(responseCode = "404", description = "Project not found")
    public ProjectDTO getProjectById(
            @Parameter(description = "Project ID") @PathParam("id") String id) {
        return projectService.getById(id);
    }

    @POST
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(
            summary = "Create a new project with media IDs",
            description = "Create a project using IDs of previously uploaded media files. Upload media files first using POST /media/upload, then use their IDs here. Requires authentication."
    )
    @APIResponse(responseCode = "201", description = "Project created successfully")
    @APIResponse(responseCode = "400", description = "Invalid data or media not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public Response createProject(CreateProjectDTO projectData) {
        ProjectDTO created = projectService.create(projectData);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Delete a project", description = "Requires authentication")
    @APIResponse(responseCode = "204", description = "Project deleted successfully")
    @APIResponse(responseCode = "404", description = "Project not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public Response deleteProject(@PathParam("id") String id) {
        projectService.delete(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/media")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(
            summary = "Associate existing media with a project",
            description = "Associate a previously uploaded media file with the specified project using its ID. Upload media first using POST /media/upload. Requires authentication."
    )
    @APIResponse(responseCode = "201", description = "Media associated successfully with the project")
    @APIResponse(responseCode = "400", description = "Invalid media ID")
    @APIResponse(responseCode = "404", description = "Project or media not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public Response addMediaToProject(
            @PathParam("id") String projectId,
            AddMediaToProjectDTO addMediaDto) {
        ProjectDTO updated = projectService.addMediaById(projectId, addMediaDto.mediaId);
        return Response.status(Response.Status.CREATED).entity(updated).build();
    }

    @DELETE
    @Path("/{id}/media/{mediaId}")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Remove media from a project", description = "Requires authentication")
    @APIResponse(responseCode = "200", description = "Media removed successfully")
    @APIResponse(responseCode = "404", description = "Project or media not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public ProjectDTO removeMedia(
            @PathParam("id") String projectId,
            @PathParam("mediaId") String mediaId) {
        return projectService.removeMedia(projectId, mediaId);
    }

    @POST
    @Path("/{id}/technologies")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Add technology to a project", description = "Requires authentication")
    @APIResponse(responseCode = "201", description = "Technology added successfully")
    @APIResponse(responseCode = "404", description = "Project not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public Response addTechnology(
            @PathParam("id") String projectId,
            TechnoDTO techno) {
        ProjectDTO updated = projectService.addTechnology(projectId, techno);
        return Response.status(Response.Status.CREATED).entity(updated).build();
    }

    @DELETE
    @Path("/{id}/technologies/{technoId}")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Remove technology from a project", description = "Requires authentication")
    @APIResponse(responseCode = "200", description = "Technology removed successfully")
    @APIResponse(responseCode = "404", description = "Project or technology not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public ProjectDTO removeTechnology(
            @PathParam("id") String projectId,
            @PathParam("technoId") String technoId) {
        return projectService.removeTechnology(projectId, technoId);
    }
}
