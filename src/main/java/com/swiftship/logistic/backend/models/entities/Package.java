package com.swiftship.logistic.backend.models.entities;

import com.swiftship.logistic.backend.models.enums.PackageStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "packages")
@Getter
@Setter
@Data
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID id;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @ManyToOne
    private Transaction transaction;

    private double weight_grams;
    private double length_cm;
    private double width_cm;
    private double height_cm;
    private PackageStatus status;

    @Column(name = "unit_value")
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "merged_packages_join",
            joinColumns = @JoinColumn(name = "merged_package_id"),
            inverseJoinColumns = @JoinColumn(name = "original_package_id")
    )
    private Set<Package> originalPackages;

    @ManyToMany(mappedBy = "originalPackages")
    private Set<Package> mergedInfo;
}
