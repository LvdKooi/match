package nl.kooi.match.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.api.dto.*;
import nl.kooi.match.api.handler.PlayerUseCaseHandler;
import nl.kooi.match.api.mapper.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/matches/{matchId}/player-events")
public class PlayerController {

    private final PlayerUseCaseHandler playerUseCaseHandler;
    private final DtoMapper mapper;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void submit(@PathVariable Long matchId,
                       @RequestBody @Valid PlayerEventRequestDto requestDto) throws Exception {
        switch (requestDto.getEventType()) {
            case LINED_UP ->
                    playerUseCaseHandler.handleUseCaseRequest(mapper.map((PlayerLineUpEventDto) requestDto, matchId));
            case SUBSTITUTED ->
                    playerUseCaseHandler.handleUseCaseRequest(mapper.map((SubstitutionEventDto) requestDto, matchId));
            case INJURED -> playerUseCaseHandler.handleUseCaseRequest(mapper.map((InjuryEventDto) requestDto, matchId));
            case YELLOW_CARD,
                    RED_CARD ->
                    playerUseCaseHandler.handleUseCaseRequest(mapper.map((DisciplineEventDto) requestDto, matchId));
        }
    }
}
