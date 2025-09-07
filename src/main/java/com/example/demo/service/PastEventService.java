package com.example.demo.service;

import com.example.demo.dto.PastEventDTO;
import com.example.demo.model.PastEvent;
import com.example.demo.model.Society;
import com.example.demo.model.Council;
import com.example.demo.repository.PastEventRepository;
import com.example.demo.repository.SocietyRepository;
import com.example.demo.repository.CouncilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PastEventService {

    private final PastEventRepository pastEventRepository;
    private final SocietyRepository societyRepository;
    private final CouncilRepository councilRepository;

    public List<PastEventDTO> getAllPastEvents() {
        log.info("Fetching all past events");
        return pastEventRepository.findAllByOrderByEventDateDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<PastEventDTO> getAllPastEvents(Pageable pageable) {
        log.info("Fetching past events with pagination");
        return pastEventRepository.findAllByOrderByEventDateDesc(pageable)
                .map(this::convertToDTO);
    }

    public Optional<PastEventDTO> getPastEventById(Long id) {
        log.info("Fetching past event with id: {}", id);
        return pastEventRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<PastEventDTO> getPastEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching past events between {} and {}", startDate, endDate);
        return pastEventRepository.findByEventDateBetweenOrderByEventDateDesc(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PastEventDTO> getEventsByMinRating(Double minRating) {
        log.info("Fetching past events with minimum rating: {}", minRating);
        return pastEventRepository.findEventsByMinRating(minRating)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PastEventDTO> getEventsByHostingBranch(String branchName) {
        log.info("Fetching past events by hosting branch: {}", branchName);
        return pastEventRepository.findEventsByHostingBranch(branchName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PastEventDTO createPastEvent(PastEventDTO eventDTO) {
        log.info("Creating new past event: {}", eventDTO.getTitle());
        PastEvent event = convertToEntity(eventDTO);
        PastEvent savedEvent = pastEventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    public Optional<PastEventDTO> updatePastEvent(Long id, PastEventDTO eventDTO) {
        log.info("Updating past event with id: {}", id);
        return pastEventRepository.findById(id)
                .map(existingEvent -> {
                    updateEventFields(existingEvent, eventDTO);
                    PastEvent updatedEvent = pastEventRepository.save(existingEvent);
                    return convertToDTO(updatedEvent);
                });
    }

    public boolean deletePastEvent(Long id) {
        log.info("Deleting past event with id: {}", id);
        if (pastEventRepository.existsById(id)) {
            pastEventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<PastEventDTO> searchEvents(String keyword) {
        log.info("Searching past events with keyword: {}", keyword);
        return pastEventRepository.searchEventsByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Object[]> getEventCountByYear() {
        log.info("Fetching event count by year");
        return pastEventRepository.getEventCountByYear();
    }

    private PastEventDTO convertToDTO(PastEvent event) {
        return new PastEventDTO(
                event.getId(),
                event.getTitle(),
                event.getEventDate(),
                event.getImage(),
                event.getDescription(),
                event.getParticipants(),
                event.getHostingBranchName(),
                event.getHostingBranchLogo(),
                event.getVenue(),
                event.getDurationHours(),
                event.getFeedbackRating(),
                event.getSociety() != null ? event.getSociety().getId() : null,
                event.getSociety() != null ? event.getSociety().getName() : null,
                event.getCouncil() != null ? event.getCouncil().getId() : null,
                event.getCouncil() != null ? event.getCouncil().getName() : null
        );
    }

    private PastEvent convertToEntity(PastEventDTO eventDTO) {
        PastEvent event = new PastEvent();
        updateEventFields(event, eventDTO);
        return event;
    }

    private void updateEventFields(PastEvent event, PastEventDTO eventDTO) {
        event.setTitle(eventDTO.getTitle());
        event.setEventDate(eventDTO.getEventDate());
        event.setImage(eventDTO.getImage());
        event.setDescription(eventDTO.getDescription());
        event.setParticipants(eventDTO.getParticipants());
        event.setHostingBranchName(eventDTO.getHostingBranchName());
        event.setHostingBranchLogo(eventDTO.getHostingBranchLogo());
        event.setVenue(eventDTO.getVenue());
        event.setDurationHours(eventDTO.getDurationHours());
        event.setFeedbackRating(eventDTO.getFeedbackRating());

        if (eventDTO.getSocietyId() != null) {
            Society society = societyRepository.findById(eventDTO.getSocietyId()).orElse(null);
            event.setSociety(society);
        }

        if (eventDTO.getCouncilId() != null) {
            Council council = councilRepository.findById(eventDTO.getCouncilId()).orElse(null);
            event.setCouncil(council);
        }
    }

    public List<PastEventDTO> getEventsBySocietyId(Long societyId) {
        log.info("Fetching past events for society id: {}", societyId);
        Society society = societyRepository.findById(societyId).orElse(null);
        if (society == null) {
            return List.of();
        }
        return pastEventRepository.findBySociety(society)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PastEventDTO> getEventsByCouncilId(Long councilId) {
        log.info("Fetching past events for council id: {}", councilId);
        Council council = councilRepository.findById(councilId).orElse(null);
        if (council == null) {
            return List.of();
        }
        return pastEventRepository.findByCouncil(council)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
