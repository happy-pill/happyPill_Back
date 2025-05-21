package com.happypill.application.repository.happyPillUser;

import com.happypill.application.entity.HappypillUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HappypillUserRepository extends JpaRepository<HappypillUser, Long> {

}
