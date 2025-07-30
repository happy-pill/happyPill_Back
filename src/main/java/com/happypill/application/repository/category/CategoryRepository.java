package com.happypill.application.repository.category;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long categoryId);

    @Query("""
            SELECT c
            FROM Category c
            """)
    List<Category> findAllCategories();

    @Query("""
            SELECT p
            FROM Product p
            WHERE p.category.id = :categoryId
            """)
    List<Product> findAllProductsInTheCategory(@Param("categoryId")Long categoryId);
}