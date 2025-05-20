package hamburg.foo.nim.game.domain.rules;

import hamburg.foo.nim.game.domain.model.GameState;
import hamburg.foo.nim.game.domain.model.PlayerType;
import hamburg.foo.nim.game.domain.model.Turn;

/**
 * Contains core game logic and rule validation for the Nim game (Misère variant). Handles turn
 * validation, next player determination, and winner calculation.
 */
public class GameRules {
    public static final int MAX_TOKENS_PER_TURN = 3;
    public static final int MIN_TOKENS_PER_TURN = 1;

    public static final String MSG_MAX_EXCEEDED =
            "You can take only up to " + MAX_TOKENS_PER_TURN + " tokens per turn.";
    public static final String MSG_BENEATH_MIN =
            "You have to take at least " + MIN_TOKENS_PER_TURN + " tokens per turn.";

    /**
     * Validates a player's move according to the game rules: - The game must still be running. -
     * Token count must be between 1 and 3. - Player cannot take more tokens than remain in the
     * game.
     *
     * @param game The current game state
     * @param turn The turn the player attempts to make
     * @return A validation result indicating whether the turn is valid or invalid, with reason
     */
    public static TurnValidationResult validateTurn(GameState game, Turn turn) {
        if (game.hasGameEnded()) {
            return TurnValidationResult.invalid("Game has already ended.");
        } else if (turn.getTakeTokens() > MAX_TOKENS_PER_TURN) {
            return TurnValidationResult.invalid(MSG_MAX_EXCEEDED);
        } else if (turn.getTakeTokens() < MIN_TOKENS_PER_TURN) {
            return TurnValidationResult.invalid(MSG_BENEATH_MIN);
        } else if (turn.getTakeTokens() > game.getRemainingTokens()) {
            return TurnValidationResult.invalid("You cannot take more tokens than available.");
        }
        return TurnValidationResult.ok();
    }

    /**
     * Returns the player who should take the next turn, alternating between COMPUTER and HUMAN.
     *
     * @param game The current game state
     * @return The next player to act
     */
    public static PlayerType getNextTurnPlayer(GameState game) {
        return game.getNextTurn() == PlayerType.COMPUTER ? PlayerType.HUMAN : PlayerType.COMPUTER;
    }

    /**
     * Determines the winner of the game when it ends. In the Misère variant, the player who did NOT
     * make the last move wins.
     *
     * @param game The final game state
     * @return The winning player
     */
    public static PlayerType getWinner(GameState game) {
        return getNextTurnPlayer(game);
    }
}
