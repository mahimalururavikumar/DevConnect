package com.devconnect.service;

import com.devconnect.config.MatchingConfig;
import com.devconnect.dto.GithubUserResponse;
import com.devconnect.dto.PageResponse;
import com.devconnect.dto.SearchRequest;
import com.devconnect.dto.SearchResponse;
import com.devconnect.entity.DeveloperProfile;
import com.devconnect.entity.Skill;
import com.devconnect.repository.DeveloperProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruiterService {

    private final DeveloperProfileRepository profileRepository;
    private final MatchingConfig matchingConfig;
    private final GithubService githubService;

    public PageResponse<SearchResponse> searchBySkills(SearchRequest request) {

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

        Page<SearchResponse> mappedPage = profiles.map(profile -> {

            Set<String> developerSkills = profile.getSkills()
                    .stream()
                    .map(Skill::getName)
                    .collect(Collectors.toSet());

            GithubUserResponse githubData = null;

            if (profile.getGithubUsername() != null) {
                githubData = githubService.getGithubUser(profile.getGithubUsername());
            }

            int matchScore = calculateMatchScore(
                    developerSkills,
                    request.getRequiredSkills()
            );

            int completionPercentage = calculateCompletion(profile);

            double finalScore = calculateFinalScore(
                    developerSkills,
                    request.getRequiredSkills(),
                    profile.getExperienceYears(),
                    completionPercentage
            );

            return SearchResponse.builder()
                    .name(profile.getUser().getName())
                    .experienceYears(profile.getExperienceYears())
                    .skills(developerSkills)
                    .matchScore(matchScore)
                    .finalScore(finalScore)
                    .githubRepos(githubData != null ? githubData.getPublic_repos() : 0)
                    .githubFollowers(githubData != null ? githubData.getFollowers() : 0)
                    .build();
        });

        // Sort by finalScore descending
        List<SearchResponse> sortedList = mappedPage.getContent()
                .stream()
                .sorted((a, b) -> Double.compare(b.getFinalScore(), a.getFinalScore()))
                .toList();


        return PageResponse.<SearchResponse>builder()
                .content(sortedList)
                .page(mappedPage.getNumber())
                .size(mappedPage.getSize())
                .totalElements(mappedPage.getTotalElements())
                .totalPages(mappedPage.getTotalPages())
                .build();
    }

    private int calculateMatchScore(Set<String> developerSkills,
                                    Set<String> requiredSkills) {

        if (requiredSkills == null || requiredSkills.isEmpty())
            return 0;

        long count = requiredSkills.stream()
                .filter(req -> developerSkills.stream()
                        .anyMatch(dev -> dev.equalsIgnoreCase(req)))
                .count();

        return (int) ((count * 100.0) / requiredSkills.size());
    }

    private double calculateFinalScore(Set<String> developerSkills,
                                       Set<String> requiredSkills,
                                       Integer experience,
                                       int completionPercentage) {

        double skillScore = calculateMatchScore(developerSkills, requiredSkills);

        double experienceScore = 0;
        if (experience != null) {
            experienceScore = Math.min(experience * 10, 100); // 10% per year capped at 100
        }

        double completionScore = completionPercentage;

        return (skillScore * matchingConfig.getSkillWeight())
                + (experienceScore * matchingConfig.getExperienceWeight())
                + (completionScore * matchingConfig.getCompletionWeight());
    }

    private int calculateCompletion(DeveloperProfile profile) {

        int score = 0;

        if (profile.getSkills() != null && !profile.getSkills().isEmpty())
            score += 25;

        if (profile.getProjects() != null && !profile.getProjects().isEmpty())
            score += 25;

        if (profile.getResumePath() != null)
            score += 25;

        if (profile.getExperienceYears() != null && profile.getExperienceYears() > 0)
            score += 25;

        return score;
    }
}