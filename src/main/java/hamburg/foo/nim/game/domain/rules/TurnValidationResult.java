package hamburg.foo.nim.game.domain.rules;

public record TurnValidationResult(boolean valid, String reason) {
    public static TurnValidationResult ok() {
        return new TurnValidationResult(true, null);
    }

    public static TurnValidationResult invalid(String reason) {
        return new TurnValidationResult(false, reason);
    }
}
