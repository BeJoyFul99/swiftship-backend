package com.swiftship.logistic.backend.models.entities;

import com.swiftship.logistic.backend.models.enums.FeeType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "transaction_fees")
@Getter
@Setter
@Data
public class TransactionFee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "fee_type", nullable = false, updatable = false, unique = true)
    private FeeType feeType;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;
}
