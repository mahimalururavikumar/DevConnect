package com.devconnect.service;

import com.devconnect.dto.GithubUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final RestTemplate restTemplate = new RestTemplate();

    public GithubUserResponse getGithubUser(String username)
    {
        String url = "https://api.github.com/users/" + username;

        return restTemplate.getForObject(
                url,
                GithubUserResponse.class
        );

    }
}
