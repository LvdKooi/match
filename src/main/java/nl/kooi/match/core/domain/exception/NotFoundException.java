package nl.kooi.match.core.domain.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String msg) {
        super(msg);
    }
}