package com.example.user;

import com.example.role.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username")
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column
    private boolean active;

    @Column
    private boolean locked;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
