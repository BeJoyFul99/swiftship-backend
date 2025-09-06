package com.swiftship.logistic.backend.repositories;

import com.swiftship.logistic.backend.models.entities.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, UUID> {
}
