package com.example.demo.service;

import com.example.demo.dto.UpcomingEventDTO;
import com.example.demo.model.UpcomingEvent;
import com.example.demo.model.Society;
import com.example.demo.model.Council;
import com.example.demo.repository.UpcomingEventRepository;
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
public class UpcomingEventService {

    private final UpcomingEventRepository upcomingEventRepository;
    private final SocietyRepository societyRepository;
    private final CouncilRepository councilRepository;

    public List<UpcomingEventDTO> getAllUpcomingEvents() {
        log.info("Fetching all upcoming events");
        return upcomingEventRepository.findAllByOrderByEventDateAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<UpcomingEventDTO> getAllUpcomingEvents(Pageable pageable) {
        log.info("Fetching upcoming events with pagination");
        return upcomingEventRepository.findAllByOrderByEventDateAsc(pageable)
                .map(this::convertToDTO);
    }

    public Optional<UpcomingEventDTO> getUpcomingEventById(Long id) {
        log.info("Fetching upcoming event with id: {}", id);
        return upcomingEventRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<UpcomingEventDTO> getUpcomingEventsAfterDate(LocalDateTime date) {
        log.info("Fetching upcoming events after date: {}", date);
        return upcomingEventRepository.findByEventDateAfterOrderByEventDateAsc(date)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UpcomingEventDTO> getEventsWithOpenRegistration() {
        log.info("Fetching events with open registration");
        return upcomingEventRepository.findEventsWithOpenRegistration(LocalDateTime.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UpcomingEventDTO> getFreeUpcomingEvents() {
        log.info("Fetching free upcoming events");
        return upcomingEventRepository.findFreeUpcomingEvents(LocalDateTime.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UpcomingEventDTO> getEventsByType(UpcomingEvent.EventType eventType) {
        log.info("Fetching events by type: {}", eventType);
        return upcomingEventRepository.findByEventType(eventType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UpcomingEventDTO createUpcomingEvent(UpcomingEventDTO eventDTO) {
        log.info("Creating new upcoming event: {}", eventDTO.getTitle());
        UpcomingEvent event = convertToEntity(eventDTO);
        UpcomingEvent savedEvent = upcomingEventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    public Optional<UpcomingEventDTO> updateUpcomingEvent(Long id, UpcomingEventDTO eventDTO) {
        log.info("Updating upcoming event with id: {}", id);
        return upcomingEventRepository.findById(id)
                .map(existingEvent -> {
                    updateEventFields(existingEvent, eventDTO);
                    UpcomingEvent updatedEvent = upcomingEventRepository.save(existingEvent);
                    return convertToDTO(updatedEvent);
                });
    }

    public boolean deleteUpcomingEvent(Long id) {
        log.info("Deleting upcoming event with id: {}", id);
        if (upcomingEventRepository.existsById(id)) {
            upcomingEventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UpcomingEventDTO> searchEvents(String keyword) {
        log.info("Searching upcoming events with keyword: {}", keyword);
        return upcomingEventRepository.searchEventsByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getUpcomingEventsCount() {
        return upcomingEventRepository.countByEventDateAfter(LocalDateTime.now());
    }

    public long getOpenRegistrationEventsCount() {
        return upcomingEventRepository.countByIsRegistrationOpenTrue();
    }

    private UpcomingEventDTO convertToDTO(UpcomingEvent event) {
        return new UpcomingEventDTO(
                event.getId(),
                event.getTitle(),
                event.getEventDate(),
                event.getImage(),
                event.getDescription(),
                event.getRegistrations(),
                event.getHostingBranchName(),
                event.getHostingBranchLogo(),
                event.getVenue(),
                event.getRegistrationDeadline(),
                event.getMaxParticipants(),
                event.getRegistrationFee(),
                event.getIsRegistrationOpen(),
                event.getEventType(),
                event.getSociety() != null ? event.getSociety().getId() : null,
                event.getSociety() != null ? event.getSociety().getName() : null,
                event.getCouncil() != null ? event.getCouncil().getId() : null,
                event.getCouncil() != null ? event.getCouncil().getName() : null
        );
    }

    private UpcomingEvent convertToEntity(UpcomingEventDTO eventDTO) {
        UpcomingEvent event = new UpcomingEvent();
        updateEventFields(event, eventDTO);
        return event;
    }

    private void updateEventFields(UpcomingEvent event, UpcomingEventDTO eventDTO) {
        event.setTitle(eventDTO.getTitle());
        event.setEventDate(eventDTO.getEventDate());
        event.setImage(eventDTO.getImage());
        event.setDescription(eventDTO.getDescription());
        event.setRegistrations(eventDTO.getRegistrations());
        event.setHostingBranchName(eventDTO.getHostingBranchName());
        event.setHostingBranchLogo(eventDTO.getHostingBranchLogo());
        event.setVenue(eventDTO.getVenue());
        event.setRegistrationDeadline(eventDTO.getRegistrationDeadline());
        event.setMaxParticipants(eventDTO.getMaxParticipants());
        event.setRegistrationFee(eventDTO.getRegistrationFee() != null ? eventDTO.getRegistrationFee() : 0.0);
        event.setIsRegistrationOpen(eventDTO.getIsRegistrationOpen() != null ? eventDTO.getIsRegistrationOpen() : true);
        event.setEventType(eventDTO.getEventType());

        if (eventDTO.getSocietyId() != null) {
            Society society = societyRepository.findById(eventDTO.getSocietyId()).orElse(null);
            event.setSociety(society);
        }

        if (eventDTO.getCouncilId() != null) {
            Council council = councilRepository.findById(eventDTO.getCouncilId()).orElse(null);
            event.setCouncil(council);
        }
    }

    public List<UpcomingEventDTO> getEventsBySocietyId(Long societyId) {
        log.info("Fetching upcoming events for society id: {}", societyId);
        Society society = societyRepository.findById(societyId).orElse(null);
        if (society == null) {
            return List.of();
        }
        return upcomingEventRepository.findBySociety(society)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UpcomingEventDTO> getEventsByCouncilId(Long councilId) {
        log.info("Fetching upcoming events for council id: {}", councilId);
        Council council = councilRepository.findById(councilId).orElse(null);
        if (council == null) {
            return List.of();
        }
        return upcomingEventRepository.findByCouncil(council)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
