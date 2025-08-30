package com.swiftship.logistic.backend.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_label")
    private String addressLabel;

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "street_address_2")
    private String streetAddress2;

    @Column(name = "apt_or_suite", nullable = false)
    private String aptOrSuite;
    @Column(nullable = false)
    private String country;

    @Column(name = "state_province", nullable = false)
    private String stateProvince;

    @Column(name = "state_city", nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_main_address")
    private boolean isMainAddress = false;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @Column(name = "is_billing_addr")
    private boolean isBilling = false;
    @Column(name = "is_shipping_addr")
    private boolean isShipping = true;
}

