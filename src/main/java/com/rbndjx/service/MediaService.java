package com.rbndjx.service;

import com.rbndjx.domain.Media;
import com.rbndjx.domain.MediaRepository;
import com.rbndjx.domain.MediaType;
import com.rbndjx.dto.MediaDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class MediaService {

    @Inject
    MediaRepository mediaRepository;

    @Inject
    S3Service s3Service;

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    private static final List<String> ALLOWED_VIDEO_TYPES = List.of(
            "video/mp4", "video/mpeg", "video/quicktime", "video/webm"
    );
    private static final List<String> ALLOWED_DOCUMENT_TYPES = List.of(
            "application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain"
    );

    @Transactional
    public MediaDTO uploadMedia(InputStream fileStream, String originalName, String mimeType, 
                                long fileSize, String uploadedBy, String alt, String folder) {
        // Validate file
        validateFile(fileSize, mimeType, originalName);

        // Generate unique filename
        String fileName = s3Service.generateUniqueFileName(originalName);

        // Determine folder based on mime type if not provided
        String targetFolder = folder != null ? folder : s3Service.getFolderByMimeType(mimeType);

        // Upload to S3
        String url = s3Service.uploadFile(fileStream, fileName, mimeType, targetFolder, fileSize);

        // Create media entity
        MediaType mediaType = Media.determineTypeFromMimeType(mimeType);
        Media media = new Media(mediaType, url, originalName, fileName, mimeType, fileSize, alt, uploadedBy);

        // Persist to database
        mediaRepository.persist(media);

        return toDTO(media);
    }

    public MediaDTO getById(String id) {
        Media media = mediaRepository.findById(id);
        if (media == null) {
            throw new NotFoundException("Media not found");
        }
        return toDTO(media);
    }

    public List<MediaDTO> getAllMedia() {
        return mediaRepository.listAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMedia(String id) {
        Media media = mediaRepository.findById(id);
        if (media == null) {
            throw new NotFoundException("Media not found");
        }

        // Delete from S3
        s3Service.deleteFileByUrl(media.url);

        // Delete from database
        mediaRepository.delete(media);
    }

    public String generateSignedUrl(String id, int expirationSeconds) {
        Media media = mediaRepository.findById(id);
        if (media == null) {
            throw new NotFoundException("Media not found");
        }

        // Extract filename and folder from URL
        String[] urlParts = media.url.split("/");
        String fileName = urlParts[urlParts.length - 1];
        String folder = urlParts.length > 4 ? urlParts[urlParts.length - 2] : null;

        return s3Service.generateSignedUrl(fileName, folder, expirationSeconds);
    }

    @Transactional
    public MediaDTO updateMetadata(String id, String alt) {
        Media media = mediaRepository.findById(id);
        if (media == null) {
            throw new NotFoundException("Media not found");
        }

        media.alt = alt;
        mediaRepository.persist(media);
        return toDTO(media);
    }

    public MediaStats getMediaStats() {
        List<Media> allMedia = mediaRepository.listAll();

        MediaStats stats = new MediaStats();
        stats.totalCount = allMedia.size();
        stats.totalSize = allMedia.stream().mapToLong(m -> m.size).sum();

        for (Media media : allMedia) {
            stats.byType.put(media.type.name(),
                    stats.byType.getOrDefault(media.type.name(), 0) + 1);
        }

        return stats;
    }

    private void validateFile(long fileSize, String mimeType, String originalName) {
        // Validate size
        if (fileSize > MAX_FILE_SIZE) {
            throw new BadRequestException(
                    "File size exceeds maximum allowed size of " + (MAX_FILE_SIZE / (1024 * 1024)) + "MB"
            );
        }

        // Validate MIME type
        List<String> allAllowedTypes = List.of();
        allAllowedTypes = Stream.concat(
                Stream.concat(ALLOWED_IMAGE_TYPES.stream(), ALLOWED_VIDEO_TYPES.stream()),
                ALLOWED_DOCUMENT_TYPES.stream()
        ).collect(Collectors.toList());

        if (!allAllowedTypes.contains(mimeType)) {
            throw new BadRequestException(
                    "File type " + mimeType + " is not allowed. Allowed types: " + String.join(", ", allAllowedTypes)
            );
        }
    }

    private MediaDTO toDTO(Media media) {
        return new MediaDTO(
                media.id,
                media.type,
                media.url,
                media.alt,
                media.originalName,
                media.fileName,
                media.mimeType,
                media.size,
                media.uploadedAt,
                media.uploadedBy
        );
    }

    public static class MediaStats {
        public int totalCount;
        public long totalSize;
        public java.util.Map<String, Integer> byType = new java.util.HashMap<>();
    }
}
