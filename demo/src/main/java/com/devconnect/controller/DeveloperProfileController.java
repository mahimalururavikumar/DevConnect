package com.devconnect.controller;

import com.devconnect.dto.DeveloperProfileRequest;
import com.devconnect.dto.DeveloperProfileResponse;
import com.devconnect.dto.ProjectRequest;
import com.devconnect.service.DeveloperProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/developer")
@RequiredArgsConstructor
public class DeveloperProfileController {

    private final DeveloperProfileService profileService;

    @PostMapping("/profile")
    public String createProfile(@RequestBody DeveloperProfileRequest request, Authentication authentication)
    {
        String email = authentication.getName();

        profileService.createProfile(email, request);

        return "Developer created Successfully";
    }

    @GetMapping("/profile")
    public DeveloperProfileResponse getProfile(Authentication authentication)
    {
        String email = authentication.getName();
        return profileService.getProfile(email);
    }

    @PostMapping("/project")
    public String addProject(@RequestBody ProjectRequest request,
                             Authentication authentication) {

        String email = authentication.getName();

        profileService.addProject(email, request);

        return "Project added successfully";
    }

    @PostMapping("/resume")
    public String uploadResume(@RequestParam("file") org.springframework.web.multipart.MultipartFile file,
                               Authentication authentication) {

        String email = authentication.getName();

        profileService.uploadResume(email, file);

        return "Resume uploaded successfully";
    }
}
