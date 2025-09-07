package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouncilDTO {
    private Long id;
    private String name;
    private String image;
    private String description;
    private String vision;
    private String mission;
    private String objectives;
    private String slateMembers; // JSON string for slate members
    private Boolean isActive;
    private String chairPerson;
    private Integer memberCount; // Slate members count
    private Integer studentMemberCount; // Student members count
    private Integer establishedYear;
    private String websiteUrl;
}
