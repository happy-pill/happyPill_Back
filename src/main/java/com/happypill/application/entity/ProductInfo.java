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
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
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

    private String contentImageUrl;

    private String description;

    private String company;

    private String briefDescription;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static ProductInfo of(Long productInfoId, String language, String name, String quantityDetails,
                                 String warningMessage, String usage, String contentImageUrl, String description,
                                 String company, String briefDescription, Product product){
        return new ProductInfo(productInfoId, Language.parseLanguage(language), name, quantityDetails, warningMessage, usage, contentImageUrl, description, company, briefDescription, product);
    }
}
