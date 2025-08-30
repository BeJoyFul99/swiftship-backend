package com.swiftship.logistic.backend.repositories;

import com.swiftship.logistic.backend.models.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
