package com.happypill.application.repository.category;

import com.happypill.application.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long categoryId);

    @Query("""
            SELECT c
            FROM Category c
            """)
    List<Category> findAllCategories();
}