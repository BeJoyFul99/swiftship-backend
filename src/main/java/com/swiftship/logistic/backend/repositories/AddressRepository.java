package com.swiftship.logistic.backend.repositories;

import com.swiftship.logistic.backend.models.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
