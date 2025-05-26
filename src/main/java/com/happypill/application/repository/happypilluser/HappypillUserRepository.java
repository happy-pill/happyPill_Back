package com.happypill.application.repository.happypilluser;

import com.happypill.application.entity.HappypillUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HappypillUserRepository extends JpaRepository<HappypillUser, Long> {
    
    Optional<HappypillUser> findBySocialSub(@Param("socialSub") String socialSub);
}
