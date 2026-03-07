package com.devconnect.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MatchingConfig {

    @Value("${matching.weight.skill}")
    private double skillWeight;

    @Value("${matching.weight.experience}")
    private double experienceWeight;

    @Value("${matching.weight.completion}")
    private double completionWeight;
}