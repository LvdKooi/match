package nl.kooi.match.api.mapper;

import nl.kooi.match.api.AnnounceMatchResponseDto;
import nl.kooi.match.api.dto.DisciplineEventDto;
import nl.kooi.match.api.dto.InjuryEventDto;
import nl.kooi.match.api.dto.PlayerLineUpEventDto;
import nl.kooi.match.api.dto.SubstitutionEventDto;
import nl.kooi.match.api.dto.match.AnnounceMatchRequestDto;
import nl.kooi.match.core.command.match.AnnounceMatchRequest;
import nl.kooi.match.core.command.match.MatchUseCaseResponse;
import nl.kooi.match.core.command.player.DisciplinePlayerRequest;
import nl.kooi.match.core.command.player.InjuredPlayerRequest;
import nl.kooi.match.core.command.player.LineUpPlayerRequest;
import nl.kooi.match.core.command.player.SubstitutePlayerRequest;
import org.mapstruct.ReportingPolicy;

@org.mapstruct.Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DtoMapper {

    DisciplinePlayerRequest map(DisciplineEventDto requestDto, Long matchId);

    InjuredPlayerRequest map(InjuryEventDto requestDto, Long matchId);

    SubstitutePlayerRequest map(SubstitutionEventDto requestDto, Long matchId);

    LineUpPlayerRequest map(PlayerLineUpEventDto requestDto, Long matchId);

    AnnounceMatchRequest map(AnnounceMatchRequestDto dto);

    AnnounceMatchResponseDto map(MatchUseCaseResponse response);
}
