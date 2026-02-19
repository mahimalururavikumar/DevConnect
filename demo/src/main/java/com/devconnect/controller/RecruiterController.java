package com.devconnect.controller;

import com.devconnect.dto.SearchRequest;
import com.devconnect.dto.SearchResponse;
import com.devconnect.service.RecruiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;

    @PostMapping("/search")
    public Page<SearchResponse> search(@RequestBody SearchRequest request) {
        return recruiterService.searchBySkills(request);
    }
}