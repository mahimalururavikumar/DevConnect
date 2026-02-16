package com.devconnect.dto;

import lombok.Data;
import java.util.Set;

@Data
public class DeveloperProfileRequest {

    private String bio;
    private Integer experienceYears;
    private String githubUsername;
    private Set<String> skills;
}
