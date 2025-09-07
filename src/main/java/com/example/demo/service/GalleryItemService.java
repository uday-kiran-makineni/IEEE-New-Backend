package com.example.demo.service;

import com.example.demo.dto.GalleryItemDTO;
import com.example.demo.model.GalleryItem;
import com.example.demo.model.PastEvent;
import com.example.demo.model.UpcomingEvent;
import com.example.demo.model.Society;
import com.example.demo.model.Council;
import com.example.demo.repository.GalleryItemRepository;
import com.example.demo.repository.PastEventRepository;
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
public class GalleryItemService {

    private final GalleryItemRepository galleryItemRepository;
    private final PastEventRepository pastEventRepository;
    private final UpcomingEventRepository upcomingEventRepository;
    private final SocietyRepository societyRepository;
    private final CouncilRepository councilRepository;

    public List<GalleryItemDTO> getAllGalleryItems() {
        log.info("Fetching all gallery items");
        return galleryItemRepository.findAllByOrderByUploadDateDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<GalleryItemDTO> getAllGalleryItems(Pageable pageable) {
        log.info("Fetching gallery items with pagination");
        return galleryItemRepository.findAllByOrderByUploadDateDesc(pageable)
                .map(this::convertToDTO);
    }

    public Optional<GalleryItemDTO> getGalleryItemById(Long id) {
        log.info("Fetching gallery item with id: {}", id);
        return galleryItemRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<GalleryItemDTO> getFeaturedGalleryItems() {
        log.info("Fetching featured gallery items");
        return galleryItemRepository.findByIsFeaturedTrueOrderByUploadDateDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GalleryItemDTO> getGalleryItemsByCategory(String category) {
        log.info("Fetching gallery items by category: {}", category);
        return galleryItemRepository.findByCategoryOrderByUploadDateDesc(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GalleryItemDTO> getGalleryItemsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching gallery items between {} and {}", startDate, endDate);
        return galleryItemRepository.findGalleryItemsByDateRange(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GalleryItemDTO> getGalleryItemsByTag(String tag) {
        log.info("Fetching gallery items by tag: {}", tag);
        return galleryItemRepository.findGalleryItemsByTag(tag)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<String> getAllCategories() {
        log.info("Fetching all distinct categories");
        return galleryItemRepository.findAllDistinctCategories();
    }

    public GalleryItemDTO createGalleryItem(GalleryItemDTO galleryItemDTO) {
        log.info("Creating new gallery item: {}", galleryItemDTO.getTitle());
        GalleryItem galleryItem = convertToEntity(galleryItemDTO);
        GalleryItem savedGalleryItem = galleryItemRepository.save(galleryItem);
        return convertToDTO(savedGalleryItem);
    }

    public Optional<GalleryItemDTO> updateGalleryItem(Long id, GalleryItemDTO galleryItemDTO) {
        log.info("Updating gallery item with id: {}", id);
        return galleryItemRepository.findById(id)
                .map(existingGalleryItem -> {
                    updateGalleryItemFields(existingGalleryItem, galleryItemDTO);
                    GalleryItem updatedGalleryItem = galleryItemRepository.save(existingGalleryItem);
                    return convertToDTO(updatedGalleryItem);
                });
    }

    public boolean deleteGalleryItem(Long id) {
        log.info("Deleting gallery item with id: {}", id);
        if (galleryItemRepository.existsById(id)) {
            galleryItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<GalleryItemDTO> searchGalleryItems(String keyword) {
        log.info("Searching gallery items with keyword: {}", keyword);
        return galleryItemRepository.searchGalleryItemsByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Object[]> getGalleryItemCountByCategory() {
        return galleryItemRepository.getGalleryItemCountByCategory();
    }

    public long getFeaturedGalleryItemsCount() {
        return galleryItemRepository.countByIsFeaturedTrue();
    }

    private GalleryItemDTO convertToDTO(GalleryItem galleryItem) {
        return new GalleryItemDTO(
                galleryItem.getId(),
                galleryItem.getImg(),
                galleryItem.getUrl(),
                galleryItem.getHeight(),
                galleryItem.getWidth(),
                galleryItem.getTitle(),
                galleryItem.getDescription(),
                galleryItem.getAltText(),
                galleryItem.getCategory(),
                galleryItem.getTags(),
                galleryItem.getIsFeatured(),
                galleryItem.getUploadDate(),
                galleryItem.getPastEvent() != null ? galleryItem.getPastEvent().getId() : null,
                galleryItem.getPastEvent() != null ? galleryItem.getPastEvent().getTitle() : null,
                galleryItem.getUpcomingEvent() != null ? galleryItem.getUpcomingEvent().getId() : null,
                galleryItem.getUpcomingEvent() != null ? galleryItem.getUpcomingEvent().getTitle() : null,
                galleryItem.getSociety() != null ? galleryItem.getSociety().getId() : null,
                galleryItem.getSociety() != null ? galleryItem.getSociety().getName() : null,
                galleryItem.getCouncil() != null ? galleryItem.getCouncil().getId() : null,
                galleryItem.getCouncil() != null ? galleryItem.getCouncil().getName() : null
        );
    }

    private GalleryItem convertToEntity(GalleryItemDTO galleryItemDTO) {
        GalleryItem galleryItem = new GalleryItem();
        updateGalleryItemFields(galleryItem, galleryItemDTO);
        return galleryItem;
    }

    private void updateGalleryItemFields(GalleryItem galleryItem, GalleryItemDTO galleryItemDTO) {
        galleryItem.setImg(galleryItemDTO.getImg());
        galleryItem.setUrl(galleryItemDTO.getUrl());
        galleryItem.setHeight(galleryItemDTO.getHeight());
        galleryItem.setWidth(galleryItemDTO.getWidth());
        galleryItem.setTitle(galleryItemDTO.getTitle());
        galleryItem.setDescription(galleryItemDTO.getDescription());
        galleryItem.setAltText(galleryItemDTO.getAltText());
        galleryItem.setCategory(galleryItemDTO.getCategory());
        galleryItem.setTags(galleryItemDTO.getTags());
        galleryItem.setIsFeatured(galleryItemDTO.getIsFeatured() != null ? galleryItemDTO.getIsFeatured() : false);
        galleryItem.setUploadDate(galleryItemDTO.getUploadDate() != null ? galleryItemDTO.getUploadDate() : LocalDateTime.now());

        if (galleryItemDTO.getPastEventId() != null) {
            PastEvent pastEvent = pastEventRepository.findById(galleryItemDTO.getPastEventId()).orElse(null);
            galleryItem.setPastEvent(pastEvent);
        }

        if (galleryItemDTO.getUpcomingEventId() != null) {
            UpcomingEvent upcomingEvent = upcomingEventRepository.findById(galleryItemDTO.getUpcomingEventId()).orElse(null);
            galleryItem.setUpcomingEvent(upcomingEvent);
        }

        if (galleryItemDTO.getSocietyId() != null) {
            Society society = societyRepository.findById(galleryItemDTO.getSocietyId()).orElse(null);
            galleryItem.setSociety(society);
        }

        if (galleryItemDTO.getCouncilId() != null) {
            Council council = councilRepository.findById(galleryItemDTO.getCouncilId()).orElse(null);
            galleryItem.setCouncil(council);
        }
    }

    public List<GalleryItemDTO> getItemsBySocietyId(Long societyId) {
        log.info("Fetching gallery items for society id: {}", societyId);
        Society society = societyRepository.findById(societyId).orElse(null);
        if (society == null) {
            return List.of();
        }
        return galleryItemRepository.findBySociety(society)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GalleryItemDTO> getItemsByCouncilId(Long councilId) {
        log.info("Fetching gallery items for council id: {}", councilId);
        Council council = councilRepository.findById(councilId).orElse(null);
        if (council == null) {
            return List.of();
        }
        return galleryItemRepository.findByCouncil(council)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
