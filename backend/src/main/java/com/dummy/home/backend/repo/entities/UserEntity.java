package com.dummy.home.backend.repo.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "USERS",
       uniqueConstraints = @UniqueConstraint(name = "UQ_USERS_EMAIL", columnNames = "EMAIL"))
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_gen")
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "USERS_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "EMAIL", nullable = false, length = 200)
    private String email;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT SYSTIMESTAMP")
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
