package com.devconnect.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ProjectRequest {

    private String title;
    private String description;
    private String techStack;
    private String projectLink;
}
