package com.devconnect.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "projects")
public class Project {

    @Id
    private long id;
    private String title;
    private String description;
    private String techStack;
    private String projectLink;

    @ManyToOne
    @JoinColumn(name = "developer_id", nullable = false)
    private DeveloperProfile developerProfile;
}
