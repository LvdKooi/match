package nl.kooi.match.api;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.player.*;
import nl.kooi.match.core.usecases.player.DisciplinePlayerUseCase;
import nl.kooi.match.core.usecases.player.InjuredPlayerUseCase;
import nl.kooi.match.core.usecases.player.LineUpPlayerUseCase;
import nl.kooi.match.core.usecases.player.SubstitutePlayerUseCase;
import nl.kooi.match.enums.ResponseType;
import nl.kooi.match.exception.PlayerEventException;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@RequiredArgsConstructor
public class UseCaseHandler {
    private final DisciplinePlayerUseCase disciplinePlayerUseCase;
    private final InjuredPlayerUseCase injuredPlayerUseCase;
    private final LineUpPlayerUseCase lineUpPlayerUseCase;
    private final SubstitutePlayerUseCase substitutePlayerUseCase;


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

    private void handleRequest(Callable<PlayerUseCaseResponse> callable) throws Exception {
        var response = callable.call();

        if (response.getResponseType() != ResponseType.PROCESSED_SUCCESSFULLY) {
            throw new PlayerEventException(String.format("Event is not valid: %s", response.getResponseType()));
        }
    }
}
