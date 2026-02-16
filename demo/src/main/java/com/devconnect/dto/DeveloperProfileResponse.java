package com.devconnect.dto;

import com.devconnect.entity.Skill;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class DeveloperProfileResponse {

    private String name;
    private String bio;
    private Integer experienceYears;
    private String githubUsername;
    private Set<String> skills;
    private Integer profileCompletionPercentage;
}
