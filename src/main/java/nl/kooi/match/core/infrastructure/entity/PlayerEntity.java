package nl.kooi.match.core.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    private TeamEntity team;
}
