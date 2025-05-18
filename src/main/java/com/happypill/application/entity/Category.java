package com.happypill.application.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "Categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Categories")
public class Category extends BaseEntity{

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    private String thumbnail;

    @OneToMany
    private List<CategoryInfo> categoryInfoList;

    @OneToMany
    private List<Product> products;
}
