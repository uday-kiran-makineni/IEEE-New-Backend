package com.example.demo.controller;

import com.example.demo.dto.GalleryItemDTO;
import com.example.demo.service.GalleryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class GalleryItemController {

    private final GalleryItemService galleryItemService;

    @GetMapping
    public ResponseEntity<List<GalleryItemDTO>> getAllGalleryItems() {
        log.info("GET /api/gallery - Fetching all gallery items");
        List<GalleryItemDTO> galleryItems = galleryItemService.getAllGalleryItems();
        return ResponseEntity.ok(galleryItems);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<GalleryItemDTO>> getAllGalleryItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        log.info("GET /api/gallery/paged - Fetching gallery items with pagination");
        Pageable pageable = PageRequest.of(page, size);
        Page<GalleryItemDTO> galleryItems = galleryItemService.getAllGalleryItems(pageable);
        return ResponseEntity.ok(galleryItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GalleryItemDTO> getGalleryItemById(@PathVariable Long id) {
        log.info("GET /api/gallery/{} - Fetching gallery item by id", id);
        return galleryItemService.getGalleryItemById(id)
                .map(galleryItem -> ResponseEntity.ok(galleryItem))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<GalleryItemDTO>> getFeaturedGalleryItems() {
        log.info("GET /api/gallery/featured - Fetching featured gallery items");
        List<GalleryItemDTO> galleryItems = galleryItemService.getFeaturedGalleryItems();
        return ResponseEntity.ok(galleryItems);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<GalleryItemDTO>> getGalleryItemsByCategory(@PathVariable String category) {
        log.info("GET /api/gallery/category/{} - Fetching gallery items by category", category);
        List<GalleryItemDTO> galleryItems = galleryItemService.getGalleryItemsByCategory(category);
        return ResponseEntity.ok(galleryItems);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        log.info("GET /api/gallery/categories - Fetching all categories");
        List<String> categories = galleryItemService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<GalleryItemDTO>> getGalleryItemsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET /api/gallery/date-range - Fetching gallery items between {} and {}", startDate, endDate);
        List<GalleryItemDTO> galleryItems = galleryItemService.getGalleryItemsByDateRange(startDate, endDate);
        return ResponseEntity.ok(galleryItems);
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<GalleryItemDTO>> getGalleryItemsByTag(@PathVariable String tag) {
        log.info("GET /api/gallery/tag/{} - Fetching gallery items by tag", tag);
        List<GalleryItemDTO> galleryItems = galleryItemService.getGalleryItemsByTag(tag);
        return ResponseEntity.ok(galleryItems);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GalleryItemDTO>> searchGalleryItems(@RequestParam String keyword) {
        log.info("GET /api/gallery/search?keyword={} - Searching gallery items", keyword);
        List<GalleryItemDTO> galleryItems = galleryItemService.searchGalleryItems(keyword);
        return ResponseEntity.ok(galleryItems);
    }

    @GetMapping("/statistics/by-category")
    public ResponseEntity<List<Object[]>> getGalleryItemCountByCategory() {
        log.info("GET /api/gallery/statistics/by-category - Fetching gallery item count by category");
        List<Object[]> statistics = galleryItemService.getGalleryItemCountByCategory();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/count/featured")
    public ResponseEntity<Long> getFeaturedGalleryItemsCount() {
        log.info("GET /api/gallery/count/featured - Fetching featured gallery items count");
        long count = galleryItemService.getFeaturedGalleryItemsCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<GalleryItemDTO> createGalleryItem(@RequestBody GalleryItemDTO galleryItemDTO) {
        log.info("POST /api/gallery - Creating new gallery item: {}", galleryItemDTO.getTitle());
        GalleryItemDTO createdGalleryItem = galleryItemService.createGalleryItem(galleryItemDTO);
        return new ResponseEntity<>(createdGalleryItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GalleryItemDTO> updateGalleryItem(@PathVariable Long id, @RequestBody GalleryItemDTO galleryItemDTO) {
        log.info("PUT /api/gallery/{} - Updating gallery item", id);
        return galleryItemService.updateGalleryItem(id, galleryItemDTO)
                .map(updatedGalleryItem -> ResponseEntity.ok(updatedGalleryItem))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGalleryItem(@PathVariable Long id) {
        log.info("DELETE /api/gallery/{} - Deleting gallery item", id);
        boolean deleted = galleryItemService.deleteGalleryItem(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
