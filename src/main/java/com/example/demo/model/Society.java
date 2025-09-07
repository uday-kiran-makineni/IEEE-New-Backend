package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "societies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Society {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String name;
    
    @Column(name = "image_url")
    private String image;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String vision;
    
    @Column(columnDefinition = "TEXT")
    private String mission;
    
    @Column(columnDefinition = "TEXT")
    private String objectives;
    
    @Column(columnDefinition = "JSON")
    private String slateMembers; // JSON array to store slate member information
    
    @Column(columnDefinition = "JSON")
    private String events; // JSON object to store events (upcoming and past)
    
    @Column(columnDefinition = "JSON")
    private String achievements; // JSON array to store achievement information
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "member_count")
    private Integer memberCount = 0; // This is now for slate members (chair, vice chair, etc.)
    
    @Column(name = "student_member_count")
    private Integer studentMemberCount = 0; // For regular student members
    
    @Column(name = "established_year")
    private Integer establishedYear;
}
