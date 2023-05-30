package nl.kooi.match.infrastructure.mapper;


import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.domain.Team;
import nl.kooi.match.infrastructure.entity.MatchEntity;
import nl.kooi.match.infrastructure.entity.PlayerEntity;
import nl.kooi.match.infrastructure.entity.PlayerEventEntity;
import nl.kooi.match.infrastructure.entity.TeamEntity;
import org.mapstruct.*;
import org.springframework.data.util.Pair;

import java.time.Instant;
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

    @Mapping(target = "team1", ignore = true)
    @Mapping(target = "team2", ignore = true)
    @BeanMapping(builder = @Builder(disableBuilder = true))
    MatchEntity map(Match match, @Context Pair<TeamEntity, TeamEntity> teams, @Context Instant creationTimestamp);

    @AfterMapping
    default void afterMappingMatch(Match match, @MappingTarget MatchEntity matchEntity, @Context Pair<TeamEntity, TeamEntity> teams, @Context Instant creationTimestamp) {
        matchEntity.setTeam1(teams.getFirst());
        matchEntity.setTeam2(teams.getSecond());
        matchEntity.setCreationTimestamp(creationTimestamp);
    }

    default String determineMatchName(MatchEntity entity) {
        return Optional.ofNullable(entity)
                .map(this::concatTeamNames)
                .orElseGet(String::new);
    }

    default String concatTeamNames(MatchEntity entity) {
        return Stream.of(entity.getTeam1(), entity.getTeam2())
                .filter(Objects::nonNull)
                .map(TeamEntity::getName)
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
