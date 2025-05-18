package com.happypill.application.entity;

import com.happypill.application.entity.enums.Language;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Category_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Category_info")
public class CategoryInfo extends BaseEntity{

    @Id
    @Column(name = "category_info_id")
    private Long categoryInfoId;

    @Enumerated(EnumType.STRING)
    private Language language;

    private String name;

    private String description;

    @Column(name = "banner_img")
    private String bannerImg;

    // FK
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
