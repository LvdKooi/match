package nl.kooi.match.core.domain;

import java.util.Set;

public record Team(Long id, String name, Set<Player> players) {
}