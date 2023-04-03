package nl.kooi.match.core.infrastructure.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany
    private Set<PlayerEntity> players;
}
