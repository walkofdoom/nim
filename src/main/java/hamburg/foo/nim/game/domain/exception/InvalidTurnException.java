package hamburg.foo.nim.game.domain.exception;

public class InvalidTurnException extends RuntimeException {
    public InvalidTurnException(String message) {
        super(message);
    }
}
