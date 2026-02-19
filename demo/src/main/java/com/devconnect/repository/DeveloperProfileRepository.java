package com.devconnect.repository;

import com.devconnect.entity.DeveloperProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DeveloperProfileRepository extends JpaRepository<DeveloperProfile, Long> {

    Optional<DeveloperProfile> findByUserId(Long userId);

    @Query("""
    SELECT DISTINCT dp
    FROM DeveloperProfile dp
    JOIN dp.skills s
    WHERE s.name IN :skills
    AND (:minExp IS NULL OR dp.experienceYears >= :minExp)
""")
    Page<DeveloperProfile> findByAnySkillAndExperience(
            @Param("skills") Set<String> skills,
            @Param("minExp") Integer minExp,
            Pageable pageable
    );


}
