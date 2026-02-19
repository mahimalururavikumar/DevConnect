package com.devconnect.dto;

import lombok.Data;
import java.util.Set;

@Data
public class SearchRequest {
    private Set<String> requiredSkills;
    private Integer minExperience;
    private int page = 0;
    private int size = 5;
}