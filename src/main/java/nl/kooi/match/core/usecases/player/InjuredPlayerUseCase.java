package nl.kooi.match.core.usecases.player;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.InjuredPlayerRequest;
import nl.kooi.match.core.command.InjuredPlayerResponse;
import nl.kooi.match.core.infrastructure.port.PlayerRepository;
import nl.kooi.match.core.usecases.UseCaseHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InjuredPlayerUseCase implements UseCaseHandler<InjuredPlayerRequest, InjuredPlayerResponse> {

    private final PlayerRepository playerRepository;

    @Override
    public InjuredPlayerResponse handle(InjuredPlayerRequest command) {
        return null;
    }
}
