package nl.kooi.match.api.mapper;

import nl.kooi.match.api.ViewMatchResponseDto;
import nl.kooi.match.api.dto.match.AnnounceMatchRequestDto;
import nl.kooi.match.api.dto.match.AnnounceMatchResponseDto;
import nl.kooi.match.api.dto.player.*;
import nl.kooi.match.core.command.match.AnnounceMatchRequest;
import nl.kooi.match.core.command.match.MatchUseCaseResponse;
import nl.kooi.match.core.command.match.ViewMatchUseCaseResponse;
import nl.kooi.match.core.command.player.*;
import nl.kooi.match.core.domain.PlayerEvent;
import org.mapstruct.ReportingPolicy;

@org.mapstruct.Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DtoMapper {

    DisciplinePlayerRequest map(DisciplineEventDto requestDto, Long matchId);

    InjuredPlayerRequest map(InjuryEventDto requestDto, Long matchId);

    SubstitutePlayerRequest map(SubstitutionEventDto requestDto, Long matchId);

    LineUpPlayerRequest map(PlayerLineUpEventDto requestDto, Long matchId);

    AnnounceMatchRequest map(AnnounceMatchRequestDto dto);

    AnnounceMatchResponseDto map(MatchUseCaseResponse response);

    ViewMatchResponseDto map(ViewMatchUseCaseResponse response);

    PlayerEventDto map(PlayerEvent event);

    PlayerScoredRequest map(PlayerScoredEventDto requestDto, Long matchId);
}
