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
@Table(name = "product_info")
public class ProductInfo extends BaseEntity{

    @Id
    private Long productInfoId;

    @Enumerated(STRING)
    private Language language;

    private String name;

    private String quantityDetails;

    private String warningMessage;

    private String usage;

    private String contentImage;

    private String description;

    private String company;

    private String briefDescription;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
