package com.rbndjx.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.Optional;

@ApplicationScoped
public class AppConfiguration {

    private static final Logger LOGGER = Logger.getLogger(AppConfiguration.class);

    @ConfigProperty(name = "aws.s3.bucket-name")
    Optional<String> s3BucketName;

    @ConfigProperty(name = "aws.cognito.user-pool-id")
    Optional<String> cognitoUserPoolId;

    @ConfigProperty(name = "aws.cognito.client-id")
    Optional<String> cognitoClientId;

    @ConfigProperty(name = "aws.cognito.region")
    Optional<String> cognitoRegion;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("🚀 Portfolio API is starting...");
        LOGGER.info("📦 S3 Bucket: " + s3BucketName.orElse("not configured"));
        LOGGER.info("🔐 Cognito User Pool: " + cognitoUserPoolId.orElse("not configured"));
        LOGGER.info("✅ Application configuration loaded");
    }
}
