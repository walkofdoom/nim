package hamburg.foo.nim.game.domain.rules;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;
import hamburg.foo.nim.game.domain.model.Turn;

public class GameRules {
    public static final int MAX_TOKENS_PER_TURN = 3;
    public static final int MIN_TOKENS_PER_TURN = 1;

    public static final String MSG_MAX_EXCEEDED = "You can take only up to " + MAX_TOKENS_PER_TURN
            + " tokens per turn.";
    public static final String MSG_BENEATH_MIN = "You have to take at least " + MIN_TOKENS_PER_TURN
            + " tokens per turn.";

    public static TurnValidationResult validateTurn(GameState game, Turn turn) {
        if (game.hasGameEnded()) {
            return TurnValidationResult.invalid("Game has already ended.");
        } else if (turn.getTakeTokens() > MAX_TOKENS_PER_TURN) {
            return TurnValidationResult.invalid(MSG_MAX_EXCEEDED);
        } else if (turn.getTakeTokens() < MIN_TOKENS_PER_TURN) {
            return TurnValidationResult
                    .invalid(MSG_BENEATH_MIN);
        } else if (turn.getTakeTokens() > game.getRemainingTokens()) {
            return TurnValidationResult.invalid("You cannot take more tokens than available.");
        }
        return TurnValidationResult.ok();
    }

    /**
     * Determine, which player has to take the next turn.
     * 
     * @param game
     * @return
     */
    public static PlayerType getNextTurnPlayer(GameState game) {
        return game.getNextTurn() == PlayerType.COMPUTER ? PlayerType.HUMAN : PlayerType.COMPUTER;
    }

    public static PlayerType getWinner(GameState game) {
        return getNextTurnPlayer(game);
    }
}
