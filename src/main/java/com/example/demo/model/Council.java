package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "councils")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Council {
    
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
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "chair_person")
    private String chairPerson;
    
    @Column(name = "member_count")
    private Integer memberCount = 0; // Slate members count
    
    @Column(name = "student_member_count")
    private Integer studentMemberCount = 0; // Student members count
    
    @Column(name = "established_year")
    private Integer establishedYear;
    
    @Column(columnDefinition = "JSON")
    private String slateMembers; // JSON array of slate members
    
    @Column(name = "website_url")
    private String websiteUrl;
}
