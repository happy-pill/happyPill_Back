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
    @Column(nullable = false)
    private Language language;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String quantityDetails;

    @Column(length = 500)
    private String warningMessage;

    private String usage;

    private String contentImageUrl;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, length = 50)
    private String company;

    @Column(nullable = false)
    private String briefDescription;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static ProductInfo of(Long productInfoId, Language language, String name, String quantityDetails,
                                          String warningMessage, String usage, String contentImage, String description,
                                          String company, String briefDescription, Product product) {
        return new ProductInfo(productInfoId, language, name, quantityDetails, warningMessage, usage, contentImage,
                description, company, briefDescription, product);
    }
    
    public static ProductInfo of(Long productInfoId, String language, String name, String quantityDetails,
                                 String warningMessage, String usage, String contentImageUrl, String description,
                                 String company, String briefDescription, Product product){
        return new ProductInfo(productInfoId, Language.parseLanguage(language), name, quantityDetails, warningMessage, usage, contentImageUrl, description, company, briefDescription, product);
    }

    public void update(String name, String briefDescription, String description, String contentImageUrl, String company, String quantityDetails, String usage, String warningMessage){
        this.name = name;
        this.briefDescription = briefDescription;
        this.description = description;
        this.contentImageUrl = contentImageUrl;
        this.company = company;
        this.quantityDetails = quantityDetails;
        this.usage = usage;
        this.warningMessage = warningMessage;
    }
}