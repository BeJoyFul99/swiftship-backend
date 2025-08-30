package com.swiftship.logistic.backend.repositories;

import com.swiftship.logistic.backend.models.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {

}
