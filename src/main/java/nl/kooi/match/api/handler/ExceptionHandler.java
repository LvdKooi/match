package nl.kooi.match.api.handler;

import nl.kooi.match.core.domain.exception.MatchEventException;
import nl.kooi.match.exception.NotFoundException;
import nl.kooi.match.exception.PlayerEventException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler({PlayerEventException.class, MatchEventException.class})
    public ProblemDetail handlePlayerEvenException(RuntimeException exc) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exc.getMessage());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ProblemDetail handlePlayerEvenException(NotFoundException exc) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exc.getMessage());
    }
}
