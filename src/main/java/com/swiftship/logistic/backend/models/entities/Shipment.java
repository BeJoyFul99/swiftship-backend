package com.swiftship.logistic.backend.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "shipments")
@Data
@Setter
@Getter
public class Shipment {
    @Id
    private String id; //Prefix: SHP-{ID}

    @OneToMany(mappedBy = "shipment")
    private Set<ShipmentEvent> shipmentEvents;

    @ManyToOne
    @JoinColumn(name = "origin_address_id", nullable = false)
    private Address originAddress;

    @ManyToOne
    @JoinColumn(name = "destination_address_id", nullable = false)
    private Address destinationAddress;

    private String carrier;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;


}
