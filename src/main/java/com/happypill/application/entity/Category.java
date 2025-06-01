package com.happypill.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "categories")
public class Category extends BaseEntity{

    @Id
    private Long categoryId;

    private String thumbnailUrl;

    private String bannerUrl;

    public static Category of(Long categoryId, String thumbnailUrl, String bannerUrl) {
        return new Category(categoryId, thumbnailUrl, bannerUrl);
    }
}
