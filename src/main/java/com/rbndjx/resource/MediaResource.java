package com.rbndjx.resource;

import com.rbndjx.dto.MediaDTO;
import com.rbndjx.service.MediaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.RestForm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Path("/media")
@Tag(name = "Media", description = "Media management endpoints")
@Produces(MediaType.APPLICATION_JSON)
public class MediaResource {

    @Inject
    MediaService mediaService;

    @GET
    @Operation(summary = "Get all media files")
    @APIResponse(responseCode = "200", description = "List of all media")
    public List<MediaDTO> getAllMedia() {
        return mediaService.getAllMedia();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get media by ID")
    @APIResponse(responseCode = "200", description = "Media retrieved successfully")
    @APIResponse(responseCode = "404", description = "Media not found")
    public MediaDTO getMediaById(@PathParam("id") String id) {
        return mediaService.getById(id);
    }

    @POST
    @Path("/upload")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(
            summary = "Upload a media file",
            description = "Upload a file to S3 and create a media entry. Returns the media ID to use when creating projects. Requires authentication."
    )
    @APIResponse(responseCode = "201", description = "Media uploaded successfully")
    @APIResponse(responseCode = "400", description = "Invalid file or file too large")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public Response uploadMedia(
            @RestForm("file") FileUpload file,
            @RestForm("alt") String alt,
            @RestForm("folder") String folder,
            @RestForm("uploadedBy") String uploadedBy) throws IOException {

        try (InputStream fileStream = Files.newInputStream(file.filePath())) {
            MediaDTO media = mediaService.uploadMedia(
                    fileStream,
                    file.fileName(),
                    file.contentType(),
                    file.size(),
                    uploadedBy,
                    alt,
                    folder
            );
            return Response.status(Response.Status.CREATED).entity(media).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Delete a media file", description = "Requires authentication")
    @APIResponse(responseCode = "204", description = "Media deleted successfully")
    @APIResponse(responseCode = "404", description = "Media not found")
    @APIResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    public Response deleteMedia(@PathParam("id") String id) {
        mediaService.deleteMedia(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/signed-url")
    @Operation(summary = "Generate a signed URL for media access")
    @APIResponse(responseCode = "200", description = "Signed URL generated successfully")
    @APIResponse(responseCode = "404", description = "Media not found")
    public Response generateSignedUrl(
            @PathParam("id") String id,
            @QueryParam("expiresIn") @DefaultValue("3600") int expiresInSeconds) {
        String signedUrl = mediaService.generateSignedUrl(id, expiresInSeconds);
        return Response.ok().entity(new SignedUrlResponse(signedUrl)).build();
    }

    @GET
    @Path("/stats")
    @Operation(summary = "Get media statistics")
    @APIResponse(responseCode = "200", description = "Media statistics")
    public MediaService.MediaStats getMediaStats() {
        return mediaService.getMediaStats();
    }

    public static class SignedUrlResponse {
        public String signedUrl;

        public SignedUrlResponse(String signedUrl) {
            this.signedUrl = signedUrl;
        }
    }
}
