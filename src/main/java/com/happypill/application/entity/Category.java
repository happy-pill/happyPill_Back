package com.happypill.application.entity;

import jakarta.persistence.Column;
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
public class Category extends BaseEntity<Long> {

    @Id
    private Long id;

    @Column(nullable = false)
    private String thumbnailUrl;

    public static Category of(Long categoryId, String thumbnailUrl) {
        return new Category(categoryId, thumbnailUrl);
    }

    public void update(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }
}
