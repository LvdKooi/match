package nl.kooi.match.core.usecases.player;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.DisciplinePlayerRequest;
import nl.kooi.match.core.command.PlayerUseCaseResponse;
import nl.kooi.match.core.usecases.UseCaseHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisciplinePlayerUseCase implements UseCaseHandler<DisciplinePlayerRequest, PlayerUseCaseResponse> {


    @Override
    public PlayerUseCaseResponse handle(DisciplinePlayerRequest command) {
        return null;
    }
}
