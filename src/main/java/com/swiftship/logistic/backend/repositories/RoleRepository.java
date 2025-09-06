package com.swiftship.logistic.backend.repositories;

import com.swiftship.logistic.backend.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
