package com.devconnect.service;

import com.devconnect.dto.SearchRequest;
import com.devconnect.dto.SearchResponse;
import com.devconnect.entity.DeveloperProfile;
import com.devconnect.entity.Skill;
import com.devconnect.repository.DeveloperProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruiterService {

    private final DeveloperProfileRepository profileRepository;

    public Page<SearchResponse> searchBySkills(SearchRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize()
        );

        Page<DeveloperProfile> profiles =
                profileRepository.findByAnySkillAndExperience(
                        request.getRequiredSkills(),
                        request.getMinExperience(),
                        pageable
                );

        return profiles.map(profile -> {

            Set<String> developerSkills = profile.getSkills()
                    .stream()
                    .map(Skill::getName)
                    .collect(Collectors.toSet());

            int matchScore = calculateMatchScore(
                    developerSkills,
                    request.getRequiredSkills()
            );

            return SearchResponse.builder()
                    .name(profile.getUser().getName())
                    .experienceYears(profile.getExperienceYears())
                    .skills(developerSkills)
                    .matchScore(matchScore)
                    .build();
        });
    }

    private int calculateMatchScore(Set<String> developerSkills, Set<String> requiredSkills) {
        if (requiredSkills.isEmpty()) return 0;

        long count = requiredSkills.stream()
                .filter(req -> developerSkills.stream()
                        .anyMatch(dev -> dev.equalsIgnoreCase(req)))
                .count();

        return (int) ((count * 100.0) / requiredSkills.size());
    }


}
