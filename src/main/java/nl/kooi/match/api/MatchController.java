package nl.kooi.match.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.api.dto.match.AnnounceMatchRequestDto;
import nl.kooi.match.api.dto.match.AnnounceMatchResponseDto;
import nl.kooi.match.api.dto.match.MatchEventRequestDto;
import nl.kooi.match.api.handler.MatchUseCaseHandler;
import nl.kooi.match.api.mapper.DtoMapper;
import nl.kooi.match.core.command.match.*;
import nl.kooi.match.core.domain.exception.MatchEventException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/matches")
public class MatchController {

    private final MatchUseCaseHandler handler;
    private final DtoMapper mapper;

    @PostMapping("/announce")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AnnounceMatchResponseDto announce(@RequestBody @Valid AnnounceMatchRequestDto requestDto) {
        return Optional.of(requestDto)
                .map(mapper::map)
                .map(handler::handle)
                .map(mapper::map)
                .orElseThrow(() -> new MatchEventException("Something went wrong"));
    }

    @PostMapping("/{matchId}/match-events")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void submit(@PathVariable Long matchId,
                       @RequestBody @Valid MatchEventRequestDto requestDto) {
        switch (requestDto.eventType()) {
            case CANCELLATION -> handler.handle(new CancelMatchRequest(matchId));
            case ENDING -> handler.handle(new EndMatchUseCaseRequest(matchId));
            case POSTPONING -> handler.handle(new PostponeMatchRequest(matchId));
            case STARTING -> handler.handle(new StartMatchRequest(matchId));
        }
    }

    @GetMapping("/{matchId}")
    public ViewMatchResponseDto viewMatch(@PathVariable Long matchId) {
        return mapper.map(handler.handle(new ViewMatchUseCaseRequest(matchId)));
    }
}
