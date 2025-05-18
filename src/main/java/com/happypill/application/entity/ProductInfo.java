package com.happypill.application.entity;


import com.happypill.application.entity.enums.Language;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ProductInfo")
public class ProductInfo extends BaseEntity{

    @Id
    @Column(name = "product_info_id")
    private Long productInfoId;

    @Enumerated(EnumType.STRING)
    private Language language;

    private String name;

    @Column(name = "quantity_details")
    private String quantityDetails;

    @Column(name = "warning_message")
    private String warningMessage;

    private String usage;

    @Column(name = "content_image")
    private String contentImage;

    private String description;

    private String company;

    @Column(name = "brief_description")
    private String briefDescription;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
