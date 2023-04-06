package nl.kooi.match.core.usecases.player;

import nl.kooi.match.core.command.LineUpPlayerRequest;
import nl.kooi.match.core.command.PlayerUseCaseResponse;
import nl.kooi.match.core.usecases.UseCaseHandler;

public class LineUpPlayerUseCase implements UseCaseHandler<LineUpPlayerRequest, PlayerUseCaseResponse> {

    @Override
    public PlayerUseCaseResponse handle(LineUpPlayerRequest command) {
        return null;
    }
}
