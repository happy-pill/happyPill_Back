package com.happypill.application.repository.categoryinfo;

import com.happypill.application.entity.CategoryInfo;
import com.happypill.application.entity.enums.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Long> {
    List<CategoryInfo> findByLanguage(Language language);

    @Query("""
            SELECT ci
            FROM CategoryInfo ci
            WHERE ci.language = :language
            """)
    Page<CategoryInfo> getAllCategoryInfo(Pageable pageable, @Param("language") Language language);

    @Query("""
            SELECT ci
            FROM CategoryInfo ci
            """)
    List<CategoryInfo> findAllCategoryInfo();
}
