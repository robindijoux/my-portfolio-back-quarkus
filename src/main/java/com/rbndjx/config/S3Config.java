package com.rbndjx.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@RegisterForReflection
public class S3Config {

    @ConfigProperty(name = "aws.region")
    String region;

    @ConfigProperty(name = "aws.credentials.access-key-id", defaultValue = "")
    String accessKeyId;

    @ConfigProperty(name = "aws.credentials.secret-access-key", defaultValue = "")
    String secretAccessKey;

    @Produces
    @ApplicationScoped
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(getCredentialsProvider())
                .httpClient(UrlConnectionHttpClient.builder().build())
                .build();
    }

    @Produces
    @ApplicationScoped
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    private AwsCredentialsProvider getCredentialsProvider() {
        // Si on est en Lambda (AWS_LAMBDA_RUNTIME_API existe) ou si les credentials sont vides,
        // utiliser DefaultCredentialsProvider qui prendra automatiquement le rôle IAM de la Lambda
        if (System.getenv("AWS_LAMBDA_RUNTIME_API") != null || 
            accessKeyId.isEmpty() || secretAccessKey.isEmpty()) {
            return DefaultCredentialsProvider.create();
        }
        
        // Sinon, utiliser les credentials statiques pour le développement local
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretAccessKey));
    }
}
