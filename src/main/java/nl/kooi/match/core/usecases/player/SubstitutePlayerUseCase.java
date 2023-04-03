package nl.kooi.match.core.usecases.player;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.SubstitutePlayerRequest;
import nl.kooi.match.core.command.SubstitutePlayerResponse;
import nl.kooi.match.core.usecases.UseCaseHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubstitutePlayerUseCase implements UseCaseHandler<SubstitutePlayerRequest, SubstitutePlayerResponse> {
    @Override
    public SubstitutePlayerResponse handle(SubstitutePlayerRequest command) {
        return null;
    }
}
