package com.happypill.application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class OrderRecipientInfo {
    @Column(nullable = false, length = 50, name = "recipient_name")
    private String name;

    @Column(nullable = false, name = "recipient_mobile")
    private String mobile;

    @Column(nullable = false, name = "recipient_email")
    private String email;


    public static OrderRecipientInfo create(String name, String mobile, String email) {
        OrderRecipientInfo orderRecipientInfo = new OrderRecipientInfo(name, mobile, email);
        orderRecipientInfo.validate();
        return orderRecipientInfo;
    }

    private void validate() {

    }

}
