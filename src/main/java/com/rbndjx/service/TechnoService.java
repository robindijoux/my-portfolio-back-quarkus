package com.rbndjx.service;

import com.rbndjx.domain.Techno;
import com.rbndjx.domain.TechnoRepository;
import com.rbndjx.dto.SimpleTechnoDTO;
import com.rbndjx.dto.TechnoDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TechnoService {

    @Inject
    TechnoRepository technoRepository;

    public List<TechnoDTO> listAll() {
        return technoRepository.listAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TechnoDTO getById(String id) {
        Techno techno = technoRepository.findById(id);
        if (techno == null) {
            throw new NotFoundException("Technology not found");
        }
        return toDTO(techno);
    }

    @Transactional
    public TechnoDTO create(SimpleTechnoDTO dto) {
        // Check if technology already exists
        Techno existing = technoRepository.findByTechnology(dto.technology);
        if (existing != null) {
            return toDTO(existing);
        }

        Techno techno = new Techno(dto.technology, dto.iconUrl);
        technoRepository.persist(techno);
        return toDTO(techno);
    }

    @Transactional
    public void delete(String id) {
        Techno techno = technoRepository.findById(id);
        if (techno == null) {
            throw new NotFoundException("Technology not found");
        }
        technoRepository.delete(techno);
    }

    @Transactional
    public TechnoDTO update(String id, SimpleTechnoDTO dto) {
        Techno techno = technoRepository.findById(id);
        if (techno == null) {
            throw new NotFoundException("Technology not found");
        }

        techno.technology = dto.technology;
        techno.iconUrl = dto.iconUrl;
        technoRepository.persist(techno);
        return toDTO(techno);
    }

    private TechnoDTO toDTO(Techno techno) {
        return new TechnoDTO(techno.id, techno.technology, techno.iconUrl);
    }
}
