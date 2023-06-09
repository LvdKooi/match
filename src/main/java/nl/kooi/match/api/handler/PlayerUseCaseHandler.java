package nl.kooi.match.api.handler;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.player.*;
import nl.kooi.match.core.usecases.player.*;
import nl.kooi.match.enums.ResponseType;
import nl.kooi.match.exception.NotFoundException;
import nl.kooi.match.exception.PlayerEventException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.Callable;

import static nl.kooi.match.enums.ResponseType.MATCH_NOT_FOUND;
import static nl.kooi.match.enums.ResponseType.PROCESSED_SUCCESSFULLY;

@Component
@RequiredArgsConstructor
public class PlayerUseCaseHandler {
    private final DisciplinePlayerUseCase disciplinePlayerUseCase;
    private final InjuredPlayerUseCase injuredPlayerUseCase;
    private final LineUpPlayerUseCase lineUpPlayerUseCase;
    private final SubstitutePlayerUseCase substitutePlayerUseCase;
    private final PlayerScoredUseCase playerScoredUseCase;


    public void handleUseCaseRequest(LineUpPlayerRequest request) throws Exception {
        handleRequest(() -> lineUpPlayerUseCase.handle(request));
    }

    public void handleUseCaseRequest(InjuredPlayerRequest request) throws Exception {
        handleRequest(() -> injuredPlayerUseCase.handle(request));
    }

    public void handleUseCaseRequest(SubstitutePlayerRequest request) throws Exception {
        handleRequest(() -> substitutePlayerUseCase.handle(request));
    }

    public void handleUseCaseRequest(DisciplinePlayerRequest request) throws Exception {
        handleRequest(() -> disciplinePlayerUseCase.handle(request));
    }

    public void handleUseCaseRequest(PlayerScoredRequest request) throws Exception {
        handleRequest(() -> playerScoredUseCase.handle(request));
    }

    private void handleRequest(Callable<PlayerUseCaseResponse> callable) throws Exception {
        Optional.ofNullable(callable.call())
                .map(PlayerUseCaseResponse::getResponseType)
                .filter(responseType -> responseType != PROCESSED_SUCCESSFULLY)
                .ifPresent(PlayerUseCaseHandler::handleNotSuccessfulResponseType);
    }

    private static void handleNotSuccessfulResponseType(ResponseType responseType) {
        if (responseType == MATCH_NOT_FOUND) {
            throw new NotFoundException(MATCH_NOT_FOUND.name());
        } else {
            throw new PlayerEventException(String.format("Event is not valid: %s", responseType));
        }
    }
}
