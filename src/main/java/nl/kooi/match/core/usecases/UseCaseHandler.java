package nl.kooi.match.core.usecases;

import jakarta.validation.Valid;

public interface UseCaseHandler<T, R> {

    R handle(@Valid T command);
}
