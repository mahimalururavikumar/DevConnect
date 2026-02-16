package com.devconnect.repository;

import com.devconnect.entity.DeveloperProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperProfileRepository extends JpaRepository<DeveloperProfile, Long> {

    Optional<DeveloperProfile> findByUserId(Long userId);
}
