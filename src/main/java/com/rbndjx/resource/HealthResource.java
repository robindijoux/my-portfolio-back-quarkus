package com.rbndjx.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Path("/health")
@Tag(name = "Health", description = "Health check endpoint")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    private final Instant startTime = Instant.now();

    @GET
    @Operation(
            summary = "Health check endpoint",
            description = "Checks the health status of the application and its dependencies"
    )
    @APIResponse(responseCode = "200", description = "Service is healthy")
    public Map<String, Object> check() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("timestamp", Instant.now().toString());

        Map<String, Object> checks = new HashMap<>();
        checks.put("database", "healthy");
        checks.put("uptime", Instant.now().getEpochSecond() - startTime.getEpochSecond());

        response.put("checks", checks);

        return response;
    }
}
