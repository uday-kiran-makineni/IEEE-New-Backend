package com.example.demo.repository;

import com.example.demo.model.GalleryItem;
import com.example.demo.model.PastEvent;
import com.example.demo.model.UpcomingEvent;
import com.example.demo.model.Society;
import com.example.demo.model.Council;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GalleryItemRepository extends JpaRepository<GalleryItem, Long> {
    
    List<GalleryItem> findAllByOrderByUploadDateDesc();
    
    Page<GalleryItem> findAllByOrderByUploadDateDesc(Pageable pageable);
    
    List<GalleryItem> findByIsFeaturedTrueOrderByUploadDateDesc();
    
    List<GalleryItem> findByCategory(String category);
    
    List<GalleryItem> findByCategoryOrderByUploadDateDesc(String category);
    
    List<GalleryItem> findByPastEvent(PastEvent pastEvent);
    
    List<GalleryItem> findByUpcomingEvent(UpcomingEvent upcomingEvent);
    
    List<GalleryItem> findBySociety(Society society);
    
    List<GalleryItem> findByCouncil(Council council);
    
    @Query("SELECT gi FROM GalleryItem gi WHERE LOWER(gi.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(gi.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(gi.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<GalleryItem> searchGalleryItemsByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT gi FROM GalleryItem gi WHERE gi.uploadDate BETWEEN :startDate AND :endDate ORDER BY gi.uploadDate DESC")
    List<GalleryItem> findGalleryItemsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                  @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT DISTINCT gi.category FROM GalleryItem gi WHERE gi.category IS NOT NULL ORDER BY gi.category")
    List<String> findAllDistinctCategories();
    
    @Query("SELECT gi FROM GalleryItem gi WHERE gi.tags LIKE CONCAT('%', :tag, '%')")
    List<GalleryItem> findGalleryItemsByTag(@Param("tag") String tag);
    
    @Query("SELECT gi.category, COUNT(gi) FROM GalleryItem gi WHERE gi.category IS NOT NULL " +
           "GROUP BY gi.category ORDER BY COUNT(gi) DESC")
    List<Object[]> getGalleryItemCountByCategory();
    
    long countByIsFeaturedTrue();
    
    long countByCategory(String category);
}
