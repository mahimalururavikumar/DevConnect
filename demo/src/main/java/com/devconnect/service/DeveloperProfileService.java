package com.devconnect.service;

import com.devconnect.dto.DeveloperProfileRequest;
import com.devconnect.dto.DeveloperProfileResponse;
import com.devconnect.entity.DeveloperProfile;
import com.devconnect.entity.Role;
import com.devconnect.entity.Skill;
import com.devconnect.entity.User;
import com.devconnect.repository.DeveloperProfileRepository;
import com.devconnect.repository.SkillRepository;
import com.devconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DeveloperProfileService {

    private final DeveloperProfileRepository profileRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public void createProfile(String email, DeveloperProfileRequest request)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.DEVELOPER) {
            throw new RuntimeException("Only developers can create profile");
        }

        if (profileRepository.findByUserId(user.getId()).isPresent()) {
            throw new RuntimeException("Profile already exists");
        }

        Set<Skill> skillEntities = new HashSet<>();

        for(String skillName : request.getSkills())
        {
            Skill skill = skillRepository.findByName(skillName)
                    .orElseGet(() -> skillRepository.save(
                            Skill.builder().name(skillName).build()
                    ));

            skillEntities.add(skill);
        }

        DeveloperProfile profile = DeveloperProfile.builder()
                .bio(request.getBio())
                .experienceYears(request.getExperienceYears())
                .githubUsername(request.getGithubUsername())
                .createdAt(LocalDateTime.now())
                .user(user)
                .skills(skillEntities)
                .build();

        profileRepository.save(profile);
    }

    public DeveloperProfileResponse getProfile(String email)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));

        DeveloperProfile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Set<String> skillNames = profile.getSkills()
                .stream()
                .map(Skill::getName)
                .collect(java.util.stream.Collectors.toSet());

        int completion = calculateCompletion(profile);

        return DeveloperProfileResponse.builder()
                .name(user.getName())
                .bio(profile.getBio())
                .experienceYears(profile.getExperienceYears())
                .githubUsername(profile.getGithubUsername())
                .skills(skillNames)
                .profileCompletionPercentage(completion)
                .build();
    }

    private int calculateCompletion(DeveloperProfile profile)
    {
        int totalFields = 4;
        int filled = 0;

        if(profile.getBio() != null && !profile.getBio().isBlank()) filled++;
        if(profile.getExperienceYears() != null) filled++;
        if(profile.getGithubUsername() != null && !profile.getGithubUsername().isBlank()) filled++;
        if(profile.getSkills() != null && !profile.getSkills().isEmpty()) filled++;

        return (filled*100)/ totalFields;
    }
}
