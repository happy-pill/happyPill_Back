package com.happypill.application.repository.categoryinfo;

import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Long> {
    List<CategoryInfo> findByLanguage(Language language);
}
