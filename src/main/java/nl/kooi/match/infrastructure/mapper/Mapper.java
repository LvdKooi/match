package nl.kooi.match.infrastructure.mapper;


import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.domain.Team;
import nl.kooi.match.infrastructure.entity.MatchEntity;
import nl.kooi.match.infrastructure.entity.PlayerEntity;
import nl.kooi.match.infrastructure.entity.PlayerEventEntity;
import nl.kooi.match.infrastructure.entity.TeamEntity;
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
                .map(this::concatTeamNames)
                .orElseGet(String::new);
    }

    default String concatTeamNames(MatchEntity entity) {
        return concatTeamNames(entity.getTeam1().getName(), entity.getTeam2().getName());
    }

    default String concatTeamNames(String teamName1, String teamName2) {
        return Stream.of(teamName1, teamName2)
                .filter(Objects::nonNull)
                .reduce("", (s1, s2) -> s1.equals("") ? s2 : s1.concat(" - ").concat(s2));
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

    TeamEntity map(Team team);

    Team map(TeamEntity team);
}
