package com.happypill.application.entity;

import com.happypill.application.entity.enums.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "category_info")
public class CategoryInfo extends BaseEntity{

    @Id
    private Long categoryInfoId;

    @Enumerated(STRING)
    private Language language;

    private String name;

    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
