package com.swiftship.logistic.backend.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Data
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
