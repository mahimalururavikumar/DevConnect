package com.devconnect.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class SearchResponse {

    private String name;
    private Integer experienceYears;
    private Set<String> skills;
    private Integer matchScore;
}