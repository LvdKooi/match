package nl.kooi.match.core.infrastructure.repository.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EntityBase {
    @Id
    Long id;
}
