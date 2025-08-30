package com.swiftship.logistic.backend.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username", unique = true, nullable = false, updatable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addressList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transaction;
}
