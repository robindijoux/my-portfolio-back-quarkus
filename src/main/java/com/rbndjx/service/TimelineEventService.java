package com.rbndjx.service;

import com.rbndjx.domain.TimelineEvent;
import com.rbndjx.domain.TimelineEventRepository;
import com.rbndjx.domain.TimelineEventType;
import com.rbndjx.dto.TimelineEventDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TimelineEventService {

    @Inject
    TimelineEventRepository timelineEventRepository;

    public List<TimelineEventDTO> listAll() {
        return timelineEventRepository.findAllOrderedByTimestamp()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TimelineEventDTO> listByType(TimelineEventType type) {
        return timelineEventRepository.findByType(type)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TimelineEventDTO getById(String id) {
        TimelineEvent event = timelineEventRepository.findById(id);
        if (event == null) {
            throw new NotFoundException("Timeline event not found");
        }
        return toDTO(event);
    }

    @Transactional
    public TimelineEventDTO create(TimelineEventDTO dto) {
        TimelineEvent event = new TimelineEvent(
                dto.timestamp,
                dto.title,
                dto.description,
                dto.type,
                dto.image,
                dto.location
        );
        timelineEventRepository.persist(event);
        return toDTO(event);
    }

    @Transactional
    public TimelineEventDTO update(String id, TimelineEventDTO dto) {
        TimelineEvent event = timelineEventRepository.findById(id);
        if (event == null) {
            throw new NotFoundException("Timeline event not found");
        }

        event.timestamp = dto.timestamp;
        event.title = dto.title;
        event.description = dto.description;
        event.type = dto.type;
        event.location = dto.location;
        event.image = dto.image;

        timelineEventRepository.persist(event);
        return toDTO(event);
    }

    @Transactional
    public void delete(String id) {
        TimelineEvent event = timelineEventRepository.findById(id);
        if (event == null) {
            throw new NotFoundException("Timeline event not found");
        }
        timelineEventRepository.delete(event);
    }

    private TimelineEventDTO toDTO(TimelineEvent event) {
        return new TimelineEventDTO(
                event.id,
                event.timestamp,
                event.title,
                event.description,
                event.type,
                event.location,
                event.image,
                event.createdAt
        );
    }
}
