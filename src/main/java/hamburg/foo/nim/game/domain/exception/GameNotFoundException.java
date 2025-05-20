package hamburg.foo.nim.game.domain.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String uuid) {
        super("Game with UUID " + uuid + " not found.");
    }
}
