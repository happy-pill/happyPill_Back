package com.happypill.application.entity;

import com.happypill.application.entity.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "category_info")
public class CategoryInfo extends BaseEntity<Long> {

    @Id
    private Long id;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Language language;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public static CategoryInfo of(Long categoryInfoId, Language language, String name, Category category) {
        return new CategoryInfo(categoryInfoId, language, name, category);
    }

    public void update(String name){
        this.name = name;
    }
}
