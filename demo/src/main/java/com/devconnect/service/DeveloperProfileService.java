package com.devconnect.service;

import com.devconnect.dto.DeveloperProfileRequest;
import com.devconnect.dto.DeveloperProfileResponse;
import com.devconnect.dto.ProjectRequest;
import com.devconnect.dto.ProjectResponse;
import com.devconnect.entity.*;
import com.devconnect.repository.DeveloperProfileRepository;
import com.devconnect.repository.SkillRepository;
import com.devconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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

        List<ProjectResponse> projectResponses =
                profile.getProjects()
                        .stream()
                        .map(p -> ProjectResponse.builder()
                                .title(p.getTitle())
                                .description(p.getDescription())
                                .techStack(p.getTechStack())
                                .projectLink(p.getProjectLink())
                                .build())
                        .toList();

        int completion = calculateCompletion(profile);

        return DeveloperProfileResponse.builder()
                .name(user.getName())
                .bio(profile.getBio())
                .experienceYears(profile.getExperienceYears())
                .githubUsername(profile.getGithubUsername())
                .skills(skillNames)
                .profileCompletionPercentage(completion)
                .projects(projectResponses)
                .build();
    }

    public void addProject(String email, ProjectRequest request)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DeveloperProfile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        Project project = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .techStack(request.getTechStack())
                .projectLink(request.getProjectLink())
                .developerProfile(profile)
                .build();

        profile.getProjects().add(project);

        profileRepository.save(profile);
    }

    public void uploadResume(String email, org.springframework.web.multipart.MultipartFile file)
    {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        if (!file.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Only PDF resumes allowed");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DeveloperProfile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        try {

            String uploadDir = "resumes/";
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadDir));

            String fileName = "user_" + user.getId() + "_resume.pdf";

            java.nio.file.Path filePath =
                    java.nio.file.Paths.get(uploadDir, fileName);

            file.transferTo(filePath);

            profile.setResumePath(filePath.toString());
            profileRepository.save(profile);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload resume");
        }
    }

    private int calculateCompletion(DeveloperProfile profile)
    {
        int totalFields = 5;
        int filled = 0;

        if(profile.getBio() != null && !profile.getBio().isBlank()) filled++;
        if(profile.getExperienceYears() != null) filled++;
        if(profile.getGithubUsername() != null && !profile.getGithubUsername().isBlank()) filled++;
        if(profile.getSkills() != null && !profile.getSkills().isEmpty()) filled++;
        if(profile.getProjects() != null && !profile.getProjects().isEmpty()) filled++;

        return (filled*100)/ totalFields;
    }
}
