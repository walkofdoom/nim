package hamburg.foo.nim.game.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the current state of a Nim game session.
 * Stores the unique ID, current turn, remaining tokens, and winner.
 */
@Data
@Builder
public class GameState {
    private String uuid;
    private PlayerType nextTurn;
    private PlayerType winner;
    private int remainingTokens;

    /**
     * Checks whether the game has ended (no tokens left).
     */
    public boolean hasGameEnded() {
        return getRemainingTokens() <= 0;
    }

    /**
     * Applies the given turn by subtracting the taken tokens from the game state.
     * 
     * @param turn The turn to apply
     */
    public void applyTurn(Turn turn) {
        this.remainingTokens -= turn.getTakeTokens();
    }
}
