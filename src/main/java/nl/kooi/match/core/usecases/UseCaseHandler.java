package nl.kooi.match.core.usecases;

public interface UseCaseHandler<T, R> {

    R handle(T command);
}
