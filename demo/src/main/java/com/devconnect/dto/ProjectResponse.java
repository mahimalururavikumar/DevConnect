package com.devconnect.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponse {

    private String title;
    private String description;
    private String techStack;
    private String projectLink;
}