package com.happypill.application.repository.happypilluser;

import com.happypill.application.entity.HappypillUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HappypillUserRepository extends JpaRepository<HappypillUser, Long> {

}
