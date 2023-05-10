package nl.kooi.match.api.handler;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.match.*;
import nl.kooi.match.core.domain.exception.MatchEventException;
import nl.kooi.match.core.usecases.match.*;
import nl.kooi.match.enums.ResponseType;
import nl.kooi.match.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

import static nl.kooi.match.enums.ResponseType.MATCH_NOT_FOUND;
import static nl.kooi.match.enums.ResponseType.PROCESSED_SUCCESSFULLY;

@Component
@RequiredArgsConstructor
public class MatchUseCaseHandler {

    private final AnnounceMatchUseCase announceMatchUseCase;
    private final CancelMatchUseCase cancelMatchUseCase;
    private final EndMatchUseCase endMatchUseCase;
    private final PostponeMatchUseCase postponeMatchUseCase;
    private final StartMatchUseCase startMatchUseCase;

    public MatchUseCaseResponse handle(AnnounceMatchRequest request) {
        var response = announceMatchUseCase.handle(request);
        handleResponse(() -> response);
        return response;
    }

    public void handle(CancelMatchRequest request) {
        handleResponse(() -> cancelMatchUseCase.handle(request));
    }

    public void handle(EndMatchUseCaseRequest request) {
        handleResponse(() -> endMatchUseCase.handle(request));
    }

    public void handle(PostponeMatchRequest request) {
        handleResponse(() -> postponeMatchUseCase.handle(request));
    }

    public void handle(StartMatchRequest request) {
        handleResponse(() -> startMatchUseCase.handle(request));
    }

    private void handleResponse(Supplier<MatchUseCaseResponse> responseSupplier) {
        Optional.ofNullable(responseSupplier)
                .map(Supplier::get)
                .map(MatchUseCaseResponse::response)
                .filter(response -> response != PROCESSED_SUCCESSFULLY)
                .ifPresent(MatchUseCaseHandler::handleNotSuccessfulResponseType);
    }

    private static void handleNotSuccessfulResponseType(ResponseType responseType) {
        if (responseType == MATCH_NOT_FOUND) {
            throw new NotFoundException("Match not found");
        }

        throw new MatchEventException(String.format("Event not valid: %s", responseType));
    }
}
