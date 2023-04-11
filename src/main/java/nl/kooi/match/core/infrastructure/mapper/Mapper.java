package nl.kooi.match.core.infrastructure.mapper;


import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.infrastructure.entity.MatchEntity;
import nl.kooi.match.core.infrastructure.entity.PlayerEntity;
import nl.kooi.match.core.infrastructure.entity.PlayerEventEntity;
import nl.kooi.match.core.infrastructure.entity.TeamEntity;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@org.mapstruct.Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface Mapper {

    @Mapping(target = "playerId", source = "player.id")
    @Mapping(target = "matchId", source = "match.id")
    PlayerEvent map(PlayerEventEntity entity);

    @Mapping(target = "player", expression = "java(mapPlayerFromPlayerEvent(event))")
    @Mapping(target = "match", expression = "java(mapMatchFromPlayerEvent(event))")
    PlayerEventEntity map(PlayerEvent event);

    @Mapping(target = "matchName", expression = "java(determineMatchName(entity))")
    Match map(MatchEntity entity);

    MatchEntity map(Match match);

    default String determineMatchName(MatchEntity entity) {
        return Optional.ofNullable(entity)
                .map(this::handleMatchName)
                .orElseGet(String::new);
    }

    default String handleMatchName(MatchEntity entity) {
        return Stream.of(entity.getTeam1(), entity.getTeam2())
                .filter(Objects::nonNull)
                .map(TeamEntity::getName)
                .reduce("", (s1, s2) -> s1.concat(" -").concat(s2));
    }

    default PlayerEntity mapPlayerFromPlayerEvent(PlayerEvent event) {
        return Optional.ofNullable(event)
                .map(PlayerEvent::getPlayerId)
                .map(id -> PlayerEntity.builder().id(id).build())
                .orElse(null);
    }

    default MatchEntity mapMatchFromPlayerEvent(PlayerEvent event) {
        return Optional.ofNullable(event)
                .map(PlayerEvent::getMatchId)
                .map(id -> MatchEntity.builder().id(id).build())
                .orElse(null);
    }
}
