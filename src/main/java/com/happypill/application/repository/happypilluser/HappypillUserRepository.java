package com.happypill.application.repository.happypilluser;

import com.happypill.application.entity.HappypillUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HappypillUserRepository extends JpaRepository<HappypillUser, Long> {
    
    Optional<HappypillUser> findBySocialSub(@Param("socialSub") String socialSub);

    @Query("""
        SELECT u
        FROM HappypillUser u
        ORDER BY u.userId DESC
""")
    Page<HappypillUser> getAllUsers(Pageable pageable);

    @Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
        FROM HappypillUser u
        WHERE (u.loginEmail = :email OR u.notifyEmail = :email)
        AND u.userId <> :userId
    """)
    boolean existEmailConflict(@Param("email") String email, @Param("userId") Long userId);
}