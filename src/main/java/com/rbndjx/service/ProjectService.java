package com.rbndjx.service;

import com.rbndjx.domain.*;
import com.rbndjx.dto.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectService {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    TechnoRepository technoRepository;

    public List<ProjectDTO> listAll() {
        return projectRepository.listAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO getById(String id) {
        Project project = projectRepository.findById(id);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        return toDTO(project);
    }

    @Transactional
    public ProjectDTO create(CreateProjectDTO dto) {
        Project project = new Project(
                dto.name,
                dto.description,
                dto.shortDescription,
                dto.repositoryLink,
                dto.projectLink,
                dto.isPublished,
                dto.featured
        );

        // Handle media IDs
        if (dto.media != null && !dto.media.isEmpty()) {
            for (String mediaId : dto.media) {
                Media media = mediaRepository.findById(mediaId);
                if (media == null) {
                    throw new NotFoundException("Media with ID " + mediaId + " not found");
                }
                project.addMedia(media);
            }
        }

        // Handle technologies
        if (dto.techStack != null && !dto.techStack.isEmpty()) {
            for (SimpleTechnoDTO techDto : dto.techStack) {
                Techno techno = technoRepository.findByTechnology(techDto.technology);
                if (techno == null) {
                    techno = new Techno(techDto.technology, techDto.iconUrl);
                    technoRepository.persist(techno);
                }
                project.addTechno(techno);
            }
        }

        projectRepository.persist(project);
        return toDTO(project);
    }

    @Transactional
    public void delete(String id) {
        Project project = projectRepository.findById(id);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }
        projectRepository.delete(project);
    }

    @Transactional
    public ProjectDTO addMediaById(String projectId, String mediaId) {
        Project project = projectRepository.findById(projectId);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }

        Media media = mediaRepository.findById(mediaId);
        if (media == null) {
            throw new NotFoundException("Media with ID " + mediaId + " not found");
        }

        // Check if media is already associated
        boolean isAlreadyAssociated = project.media.stream()
                .anyMatch(m -> m.id.equals(mediaId));
        if (isAlreadyAssociated) {
            throw new IllegalArgumentException("Media is already associated with this project");
        }

        project.addMedia(media);
        projectRepository.persist(project);
        return toDTO(project);
    }

    @Transactional
    public ProjectDTO removeMedia(String projectId, String mediaId) {
        Project project = projectRepository.findById(projectId);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }

        Media media = project.media.stream()
                .filter(m -> m.id.equals(mediaId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Media not found in project"));

        project.removeMedia(media);
        mediaRepository.delete(media);
        projectRepository.persist(project);
        return toDTO(project);
    }

    @Transactional
    public ProjectDTO addTechnology(String projectId, TechnoDTO technoDTO) {
        Project project = projectRepository.findById(projectId);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }

        Techno techno = technoRepository.findById(technoDTO.id);
        if (techno == null) {
            techno = new Techno(technoDTO.technology, technoDTO.iconUrl);
            techno.id = technoDTO.id;
            technoRepository.persist(techno);
        }

        project.addTechno(techno);
        projectRepository.persist(project);
        return toDTO(project);
    }

    @Transactional
    public ProjectDTO removeTechnology(String projectId, String technoId) {
        Project project = projectRepository.findById(projectId);
        if (project == null) {
            throw new NotFoundException("Project not found");
        }

        Techno techno = project.techStack.stream()
                .filter(t -> t.id.equals(technoId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Technology not found in project"));

        project.removeTechno(techno);
        projectRepository.persist(project);
        return toDTO(project);
    }

    private ProjectDTO toDTO(Project project) {
        ProjectDTO dto = new ProjectDTO(
                project.id,
                project.name,
                project.description,
                project.shortDescription,
                project.isPublished,
                project.featured,
                project.views,
                project.repositoryLink,
                project.projectLink,
                project.createdAt
        );

        dto.media = project.media.stream()
                .map(this::mediaToDTO)
                .collect(Collectors.toList());

        dto.techStack = project.techStack.stream()
                .map(this::technoToDTO)
                .collect(Collectors.toList());

        return dto;
    }

    private MediaDTO mediaToDTO(Media media) {
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

    private TechnoDTO technoToDTO(Techno techno) {
        return new TechnoDTO(techno.id, techno.technology, techno.iconUrl);
    }
}
